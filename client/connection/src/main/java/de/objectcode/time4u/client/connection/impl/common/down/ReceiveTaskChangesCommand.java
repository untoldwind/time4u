package de.objectcode.time4u.client.connection.impl.common.down;

import static java.lang.Math.min;
import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.IServerConnectionRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.filter.TaskFilter;

public class ReceiveTaskChangesCommand implements ISynchronizationCommand
{
  private final static int REVISION_CHUNK = 10;

  /**
   * {@inheritDoc}
   */
  public boolean shouldRun(final SynchronizationContext context)
  {
    return context.getServerRevisionStatus(SynchronizableType.TASK) > context.getSynchronizationStatus(
        SynchronizableType.TASK).getLastReceivedRevision();
  }

  /**
   * {@inheritDoc}
   */
  public void execute(final SynchronizationContext context) throws RepositoryException, ConnectionException
  {
    final long clientId = context.getRepository().getClientId();
    final ITaskRepository taskRepository = context.getRepository().getTaskRepository();
    final ITaskService taskService = context.getTaskService();
    final IServerConnectionRepository serverConnectionRepository = context.getRepository()
        .getServerConnectionRepository();
    final TaskFilter filter = new TaskFilter();
    filter.setOrder(TaskFilter.Order.ID);

    final SynchronizationStatus status = context.getSynchronizationStatus(SynchronizableType.TASK);
    final long currentRemoteRevision = context.getServerRevisionStatus(SynchronizableType.TASK);

    // Do in chunks to avoid extremely large result sets
    for (long beginRevision = status.getLastReceivedRevision() + 1; beginRevision <= currentRemoteRevision; beginRevision += REVISION_CHUNK) {
      filter.setMinRevision(beginRevision);
      filter.setMaxRevision(min(beginRevision + REVISION_CHUNK, currentRemoteRevision));

      final FilterResult<Task> tasks = taskService.getTasks(filter);

      // Empty result might by null
      if (tasks != null && tasks.getResults() != null) {
        for (final Task task : tasks.getResults()) {
          // Ignore changes made by myself
          if (task.getLastModifiedByClient() != clientId) {
            taskRepository.storeTask(task, false);
          }
        }
      }

      status.setLastReceivedRevision(filter.getMaxRevision());
      serverConnectionRepository.storeSynchronizationStatus(context.getServerConnectionId(), status);
    }
  }
}