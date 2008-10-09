package de.objectcode.time4u.client.connection.impl.common.up;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.filter.TaskFilter;

/**
 * Send task changes to the server.
 * 
 * @author junglas
 */
public class SendTaskChangesCommand extends BaseSendCommand<Task>
{
  public SendTaskChangesCommand()
  {
    super(EntityType.TASK);
  }

  @Override
  protected List<Task> queryEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws RepositoryException
  {
    final TaskFilter filter = new TaskFilter();
    // Only send changes made by myself
    filter.setLastModifiedByClient(context.getRepository().getClientId());

    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(TaskFilter.Order.ID);

    return context.getRepository().getTaskRepository().getTasks(filter);
  }

  @Override
  protected void sendEntity(final SynchronizationContext context, final Task entity) throws ConnectionException
  {
    context.getTaskService().storeTask(entity);
  }
}