package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.DayInfoSummary;

public class DayInfoRepositoryEvent extends RepositoryEvent
{
  private final DayInfoSummary m_dayInfo;

  public DayInfoRepositoryEvent(final DayInfoSummary dayInfo)
  {
    m_dayInfo = dayInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.DAYINFO;
  }

  public DayInfoSummary getDayInfo()
  {
    return m_dayInfo;
  }

}
