package de.objectcode.time4u.client.store.api.event;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.api.data.TaskSummary;

public class TaskRepositoryEvent extends RepositoryEvent
{
  private final List<TaskSummary> m_tasks;

  public TaskRepositoryEvent(final List<? extends TaskSummary> tasks)
  {
    m_tasks = new ArrayList<TaskSummary>(tasks);
  }

  public TaskRepositoryEvent(final TaskSummary task)
  {
    m_tasks = new ArrayList<TaskSummary>();
    m_tasks.add(task);
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
