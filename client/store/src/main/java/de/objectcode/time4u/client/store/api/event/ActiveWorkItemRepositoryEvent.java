package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.WorkItem;

public class ActiveWorkItemRepositoryEvent extends RepositoryEvent
{
  private final WorkItem m_activeWorkItem;

  public ActiveWorkItemRepositoryEvent(final WorkItem activeWorkItem)
  {
    m_activeWorkItem = activeWorkItem;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.ACTIVE_WORKITEM;
  }

  public WorkItem getActiveWorkItem()
  {
    return m_activeWorkItem;
  }
}
