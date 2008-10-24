package de.objectcode.time4u.client.store.api.event;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.api.data.DayInfoSummary;

public class DayInfoRepositoryEvent extends RepositoryEvent
{
  private final List<DayInfoSummary> m_dayInfos;

  public DayInfoRepositoryEvent(final List<? extends DayInfoSummary> dayInfos)
  {
    m_dayInfos = new ArrayList<DayInfoSummary>(dayInfos);
  }

  public DayInfoRepositoryEvent(final DayInfoSummary dayInfo)
  {
    m_dayInfos = new ArrayList<DayInfoSummary>();
    m_dayInfos.add(dayInfo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.DAYINFO;
  }

  public List<DayInfoSummary> getDayInfos()
  {
    return m_dayInfos;
  }

}
