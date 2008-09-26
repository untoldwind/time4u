package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.TaskSummary;

public class TaskRepositoryEvent extends RepositoryEvent
{
  TaskSummary m_task;

  public TaskRepositoryEvent(final TaskSummary task)
  {
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
