package de.objectcode.time4u.client.connection.impl.common.up;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

/**
 * Send project changes to the server.
 * 
 * @author junglas
 */
public class SendProjectChangesCommand extends BaseSendCommand<Project>
{
  public SendProjectChangesCommand()
  {
    super(EntityType.PROJECT);
  }

  @Override
  protected List<Project> queryEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws RepositoryException
  {
    final ProjectFilter filter = new ProjectFilter();
    // Only send changes made by myself or by clients not known to the server
    if (context.getRegisteredClientIds() == null) {
      filter.setLastModifiedByClient(context.getRepository().getClientId());
    }
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(ProjectFilter.Order.ID);

    List<Project> projects = context.getRepository().getProjectRepository().getProjects(filter);

    if (context.getRegisteredClientIds() != null) {
      projects = filterByClientId(projects, context.getRepository().getClientId(), context.getRegisteredClientIds());
    }

    if (context.getRootProject() != null) {
      final List<Project> filteredProjects = new ArrayList<Project>();
      final String rootProjectId = context.getRootProject().getId();

      for (final Project project : projects) {
        if (project.getParentId() == null) {
          continue;
        }

        if (rootProjectId.equals(project.getParentId())) {
          project.setParentId(null);
          filteredProjects.add(project);
          continue;
        }

        ProjectSummary current = project;

        while (current != null && !rootProjectId.equals(current.getParentId())) {
          if (current.getParentId() == null) {
            current = null;
          } else {
            current = context.getRepository().getProjectRepository().getProjectSummary(current.getParentId());
          }
        }
        if (current != null) {
          filteredProjects.add(project);
        }
      }

      return filteredProjects;
    }

    return projects;
  }

  @Override
  protected void sendEntity(final SynchronizationContext context, final Project entity) throws ConnectionException
  {
    context.getProjectService().storeProject(entity);
  }

  private List<Project> filterByClientId(final List<Project> projects, final long selfClientId,
      final Set<Long> registeredClientIds)
  {
    final List<Project> filteredProjects = new ArrayList<Project>();

    for (final Project project : projects) {
      if (selfClientId == project.getLastModifiedByClient()
          || !registeredClientIds.contains(project.getLastModifiedByClient())) {
        project.setLastModifiedByClient(selfClientId);
        filteredProjects.add(project);
      }
    }
    return filteredProjects;
  }
}
