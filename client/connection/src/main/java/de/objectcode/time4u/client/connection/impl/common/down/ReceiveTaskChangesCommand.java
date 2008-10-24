package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.filter.TaskFilter;

public class ReceiveTaskChangesCommand extends BaseReceiveCommand<Task>
{
  public ReceiveTaskChangesCommand()
  {
    super(EntityType.TASK);
  }

  @Override
  protected List<Task> receiveEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws ConnectionException
  {
    final TaskFilter filter = new TaskFilter();
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(TaskFilter.Order.ID);

    return context.getTaskService().getTasks(filter).getResults();
  }

  @Override
  protected void storeEntity(final SynchronizationContext context, final Task entity) throws RepositoryException
  {
    context.getRepository().getTaskRepository().storeTask(entity, false);
  }
}