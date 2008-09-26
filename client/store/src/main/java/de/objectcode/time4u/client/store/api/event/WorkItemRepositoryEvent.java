package de.objectcode.time4u.client.store.api.event;

import java.util.List;

import de.objectcode.time4u.server.api.data.WorkItem;

public class WorkItemRepositoryEvent extends RepositoryEvent
{
  List<WorkItem> m_workItems;

  public WorkItemRepositoryEvent(final List<WorkItem> workItems)
  {
    m_workItems = workItems;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.WORKITEM;
  }

  public List<WorkItem> getWorkItems()
  {
    return m_workItems;
  }
}
