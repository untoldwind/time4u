package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.Task;

public class TaskRepositoryEvent extends RepositoryEvent
{
  Task m_task;

  public TaskRepositoryEvent(final Task task)
  {
    super();
    m_task = task;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.TASK;
  }
}
