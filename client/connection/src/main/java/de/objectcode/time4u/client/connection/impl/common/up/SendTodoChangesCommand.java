package de.objectcode.time4u.client.connection.impl.common.up;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class SendTodoChangesCommand extends BaseSendCommand<TodoSummary>
{
  public SendTodoChangesCommand()
  {
    super(EntityType.TODO);
  }

  @Override
  protected List<TodoSummary> queryEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws RepositoryException
  {
    final TodoFilter filter = new TodoFilter();
    // Only send changes made by myself
    filter.setLastModifiedByClient(context.getRepository().getClientId());

    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(TodoFilter.Order.ID);

    return context.getRepository().getTodoRepository().getTodos(filter, false);
  }

  @Override
  protected void sendEntity(final SynchronizationContext context, final TodoSummary entity) throws ConnectionException
  {
    if (entity instanceof Todo) {
      context.getTodoService().storeTodo((Todo) entity);
    } else if (entity instanceof TodoGroup) {
      context.getTodoService().storeTodoGroup((TodoGroup) entity);
    }
  }
}