package de.objectcode.time4u.client.connection.impl.common.up;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoAssignment;
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
    // Only send changes made by myself or by clients not known to the server
    if (context.getRegisteredClientIds() == null) {
      filter.setLastModifiedByClient(context.getRepository().getClientId());
    }
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(TodoFilter.Order.ID);

    List<TodoSummary> todos = context.getRepository().getTodoRepository().getTodos(filter, false);

    if (context.getRegisteredClientIds() != null) {
      todos = filterByClientId(todos, context.getRepository().getClientId(), context.getRegisteredClientIds());
    }

    if (context.getRootProject() != null) {
      final List<TodoSummary> filteredTodos = new ArrayList<TodoSummary>();
      final String rootProjectId = context.getRootProject().getId();

      for (final TodoSummary todoSummary : todos) {
        if (todoSummary.isGroup()) {
          filteredTodos.add(todoSummary);
          continue;
        }

        if (((Todo) todoSummary).getTaskId() == null) {
          filteredTodos.add(todoSummary);
          continue;
        }

        final TaskSummary task = context.getRepository().getTaskRepository().getTaskSummary(
            ((Todo) todoSummary).getTaskId());
        ProjectSummary current = context.getRepository().getProjectRepository().getProjectSummary(task.getProjectId());

        while (current != null && !rootProjectId.equals(current.getParentId())) {
          if (current.getParentId() == null) {
            current = null;
          } else {
            current = context.getRepository().getProjectRepository().getProjectSummary(current.getParentId());
          }
        }
        if (current != null) {
          filteredTodos.add(todoSummary);
        }
      }
      return filteredTodos;
    }

    return todos;
  }

  @Override
  protected void sendEntity(final SynchronizationContext context, final TodoSummary entity) throws ConnectionException
  {
    if (entity instanceof Todo) {
      if (context.getMappedPersonId() != null) {
        final String ownerId = context.getRepository().getOwner().getId();

        if (ownerId.equals(entity.getReporterId())) {
          entity.setReporterId(context.getMappedPersonId());
        }
        final Todo todo = (Todo) entity;

        if (todo.getVisibleToPersonIds() != null) {
          final int idx = todo.getVisibleToPersonIds().indexOf(ownerId);

          if (idx >= 0) {
            todo.getVisibleToPersonIds().remove(idx);
            todo.getVisibleToPersonIds().add(context.getMappedPersonId());
          }
        }

        if (todo.getAssignments() != null) {
          for (final TodoAssignment todoAssignment : todo.getAssignments()) {
            if (ownerId.equals(todoAssignment.getPersonId())) {
              todoAssignment.setPersonId(context.getMappedPersonId());
            }
          }
        }
      }

      context.getTodoService().storeTodo((Todo) entity);
    } else if (entity instanceof TodoGroup) {
      if (context.getMappedPersonId() != null) {
        final String ownerId = context.getRepository().getOwner().getId();

        if (ownerId.equals(entity.getReporterId())) {
          entity.setReporterId(context.getMappedPersonId());
        }
        final TodoGroup todoGroup = (TodoGroup) entity;

        if (todoGroup.getVisibleToPersonIds() != null) {
          final int idx = todoGroup.getVisibleToPersonIds().indexOf(ownerId);

          if (idx >= 0) {
            todoGroup.getVisibleToPersonIds().remove(idx);
            todoGroup.getVisibleToPersonIds().add(context.getMappedPersonId());
          }
        }
      }

      context.getTodoService().storeTodoGroup((TodoGroup) entity);
    }
  }

  private List<TodoSummary> filterByClientId(final List<TodoSummary> todos, final long selfClientId,
      final Set<Long> registeredClientIds)
  {
    final List<TodoSummary> filteredTodos = new ArrayList<TodoSummary>();

    for (final TodoSummary todo : todos) {
      if (selfClientId == todo.getLastModifiedByClient()
          || !registeredClientIds.contains(todo.getLastModifiedByClient())) {
        // Simulate a change by self (i.e. client is man in the middle)
        todo.setLastModifiedByClient(selfClientId);
        filteredTodos.add(todo);
      }
    }
    return filteredTodos;
  }
}