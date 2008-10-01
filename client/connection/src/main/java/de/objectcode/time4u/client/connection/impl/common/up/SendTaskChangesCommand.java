package de.objectcode.time4u.client.connection.impl.common.up;

import static java.lang.Math.min;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.IServerConnectionRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.filter.TaskFilter;

public class SendTaskChangesCommand implements ISynchronizationCommand
{
  private final static int REVISION_CHUNK = 10;

  /**
   * {@inheritDoc}
   */
  public boolean shouldRun(final SynchronizationContext context)
  {
    return context.getClientRevisionStatus(SynchronizableType.TASK) > context.getSynchronizationStatus(
        SynchronizableType.TASK).getLastSendRevision();
  }

  /**
   * {@inheritDoc}
   */
  public void execute(final SynchronizationContext context) throws RepositoryException, ConnectionException
  {
    final ITaskRepository taskRepository = context.getRepository().getTaskRepository();
    final ITaskService taskService = context.getTaskService();
    final IServerConnectionRepository serverConnectionRepository = context.getRepository()
        .getServerConnectionRepository();

    final TaskFilter filter = new TaskFilter();
    // Only send changes made by myself
    filter.setLastModifiedByClient(context.getRepository().getClientId());
    filter.setOrder(TaskFilter.Order.ID);

    final SynchronizationStatus status = context.getSynchronizationStatus(SynchronizableType.TASK);
    final long currentLocalRevision = context.getClientRevisionStatus(SynchronizableType.TASK);

    // Do in chunks to avoid extremely large result sets
    for (long beginRevision = status.getLastSendRevision() + 1; beginRevision <= currentLocalRevision; beginRevision += REVISION_CHUNK) {
      filter.setMinRevision(beginRevision);
      filter.setMaxRevision(min(beginRevision + REVISION_CHUNK, currentLocalRevision));

      final List<Task> tasks = taskRepository.getTasks(filter);

      for (final Task task : tasks) {
        taskService.storeTask(task);
      }

      status.setLastSendRevision(filter.getMaxRevision());
      serverConnectionRepository.storeSynchronizationStatus(context.getServerConnectionId(), status);
    }
  }
}