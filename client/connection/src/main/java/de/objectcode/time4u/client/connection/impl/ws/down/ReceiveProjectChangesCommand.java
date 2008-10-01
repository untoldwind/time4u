package de.objectcode.time4u.client.connection.impl.ws.down;

import static java.lang.Math.min;
import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.ws.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.ws.SynchronizationContext;
import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.IServerConnectionRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

/**
 * Receive project changes from the server.
 * 
 * @author junglas
 */
public class ReceiveProjectChangesCommand implements ISynchronizationCommand
{
  private final static int REVISION_CHUNK = 10;

  /**
   * {@inheritDoc}
   */
  public boolean shouldRun(final SynchronizationContext context)
  {
    return context.getServerRevisionStatus(SynchronizableType.PROJECT) > context.getSynchronizationStatus(
        SynchronizableType.PROJECT).getLastReceivedRevision();
  }

  /**
   * {@inheritDoc}
   */
  public void execute(final SynchronizationContext context) throws RepositoryException, ConnectionException
  {
    final long clientId = context.getRepository().getClientId();
    final IProjectRepository projectRepository = context.getRepository().getProjectRepository();
    final IProjectService projectService = context.getProjectService();
    final IServerConnectionRepository serverConnectionRepository = context.getRepository()
        .getServerConnectionRepository();
    final ProjectFilter filter = new ProjectFilter();
    filter.setOrder(ProjectFilter.Order.ID);

    final SynchronizationStatus status = context.getSynchronizationStatus(SynchronizableType.PROJECT);
    final long currentRemoteRevision = context.getServerRevisionStatus(SynchronizableType.PROJECT);

    // Do in chunks to avoid extremely large result sets
    for (long beginRevision = status.getLastReceivedRevision() + 1; beginRevision <= currentRemoteRevision; beginRevision += REVISION_CHUNK) {
      filter.setMinRevision(beginRevision);
      filter.setMaxRevision(min(beginRevision + REVISION_CHUNK, currentRemoteRevision));

      final FilterResult<Project> projects = projectService.getProjects(filter);

      // Empty result might by null
      if (projects != null && projects.getResults() != null) {
        for (final Project project : projects.getResults()) {
          // Ignore changes made by myself
          if (project.getLastModifiedByClient() != clientId) {
            projectRepository.storeProject(project, false);
          }
        }
      }

      status.setLastReceivedRevision(filter.getMaxRevision());
      serverConnectionRepository.storeSynchronizationStatus(context.getServerConnectionId(), status);
    }
  }
}
