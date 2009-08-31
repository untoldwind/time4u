package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoAssignment;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class ReceiveTodoChangesCommand extends BaseReceiveCommand<TodoSummary>
{
  public ReceiveTodoChangesCommand()
  {
    super(EntityType.TODO);
  }

  @Override
  protected List<TodoSummary> receiveEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws ConnectionException
  {
    final TodoFilter filter = new TodoFilter();
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(TodoFilter.Order.ID);

    return context.getTodoService().getTodos(filter).getResults();
  }

  @Override
  protected void storeEntity(final SynchronizationContext context, final TodoSummary entity) throws RepositoryException
  {
    if (entity instanceof Todo) {
      if (context.getMappedPersonId() != null) {
        if (context.getMappedPersonId().equals(entity.getReporterId())) {
          entity.setReporterId(context.getRepository().getOwner().getId());
        }
        final Todo todo = (Todo) entity;

        if (todo.getVisibleToPersonIds() != null) {
          final int idx = todo.getVisibleToPersonIds().indexOf(context.getMappedPersonId());

          if (idx >= 0) {
            todo.getVisibleToPersonIds().remove(idx);
            todo.getVisibleToPersonIds().add(context.getRepository().getOwner().getId());
          }
        }

        if (todo.getAssignments() != null) {
          for (final TodoAssignment todoAssignment : todo.getAssignments()) {
            if (context.getMappedPersonId().equals(todoAssignment.getPersonId())) {
              todoAssignment.setPersonId(context.getRepository().getOwner().getId());
            }
          }
        }
      }

      context.getRepository().getTodoRepository().storeTodo((Todo) entity, false);
    } else if (entity instanceof TodoGroup) {
      if (context.getMappedPersonId() != null) {
        if (context.getMappedPersonId().equals(entity.getReporterId())) {
          entity.setReporterId(context.getRepository().getOwner().getId());
        }
        final TodoGroup todoGroup = (TodoGroup) entity;

        if (todoGroup.getVisibleToPersonIds() != null) {
          final int idx = todoGroup.getVisibleToPersonIds().indexOf(context.getMappedPersonId());

          if (idx >= 0) {
            todoGroup.getVisibleToPersonIds().remove(idx);
            todoGroup.getVisibleToPersonIds().add(context.getRepository().getOwner().getId());
          }
        }
      }

      context.getRepository().getTodoRepository().storeTodoGroup((TodoGroup) entity, false);
    }
  }
}
