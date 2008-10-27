package de.objectcode.time4u.client.store.api.event;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.api.data.Todo;

public class TodoRepositoryEvent extends RepositoryEvent
{
  private final List<Todo> m_tasks;

  public TodoRepositoryEvent(final List<? extends Todo> tasks)
  {
    m_tasks = new ArrayList<Todo>(tasks);
  }

  public TodoRepositoryEvent(final Todo todo)
  {
    m_tasks = new ArrayList<Todo>();
    m_tasks.add(todo);
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
