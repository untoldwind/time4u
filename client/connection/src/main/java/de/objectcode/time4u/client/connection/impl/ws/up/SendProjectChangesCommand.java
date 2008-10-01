package de.objectcode.time4u.client.connection.impl.ws.up;

import static java.lang.Math.min;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.ws.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.ws.SynchronizationContext;
import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.IServerConnectionRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

/**
 * Send project changes to the server.
 * 
 * @author junglas
 */
public class SendProjectChangesCommand implements ISynchronizationCommand
{
  private final static int REVISION_CHUNK = 10;

  /**
   * {@inheritDoc}
   */
  public boolean shouldRun(final SynchronizationContext context)
  {
    return context.getClientRevisionStatus(SynchronizableType.PROJECT) > context.getSynchronizationStatus(
        SynchronizableType.PROJECT).getLastSendRevision();
  }

  /**
   * {@inheritDoc}
   */
  public void execute(final SynchronizationContext context) throws RepositoryException, ConnectionException
  {
    final IProjectRepository projectRepository = context.getRepository().getProjectRepository();
    final IProjectService projectService = context.getProjectService();
    final IServerConnectionRepository serverConnectionRepository = context.getRepository()
        .getServerConnectionRepository();

    final ProjectFilter filter = new ProjectFilter();
    // Only send changes made by myself
    filter.setLastModifiedByClient(context.getRepository().getClientId());

    final SynchronizationStatus status = context.getSynchronizationStatus(SynchronizableType.PROJECT);
    final long currentLocalRevision = context.getClientRevisionStatus(SynchronizableType.PROJECT);

    // Do in chunks to avoid extremely large result sets
    for (long beginRevision = status.getLastSendRevision() + 1; beginRevision <= currentLocalRevision; beginRevision += REVISION_CHUNK) {
      filter.setMinRevision(beginRevision);
      filter.setMaxRevision(min(beginRevision + REVISION_CHUNK, currentLocalRevision));

      final List<Project> projects = projectRepository.getProjects(filter);

      for (final Project project : projects) {
        projectService.storeProject(project);
      }

      status.setLastSendRevision(filter.getMaxRevision());
      serverConnectionRepository.storeSynchronizationStatus(context.getServerConnectionId(), status);
    }
  }

}
