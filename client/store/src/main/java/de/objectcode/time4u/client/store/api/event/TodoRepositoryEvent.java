package de.objectcode.time4u.client.store.api.event;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoRepositoryEvent extends RepositoryEvent
{
  private final List<TodoSummary> m_todos;

  public TodoRepositoryEvent(final List<? extends TodoSummary> todos)
  {
    m_todos = new ArrayList<TodoSummary>(todos);
  }

  public TodoRepositoryEvent(final TodoSummary todo)
  {
    m_todos = new ArrayList<TodoSummary>();
    m_todos.add(todo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.TODO;
  }
}
