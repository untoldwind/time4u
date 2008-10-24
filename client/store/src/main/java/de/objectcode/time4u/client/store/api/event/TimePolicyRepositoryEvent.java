package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.TimePolicy;

public class TimePolicyRepositoryEvent extends RepositoryEvent
{
  TimePolicy m_timePolicy;

  public TimePolicyRepositoryEvent(final TimePolicy timePolicy)
  {
    m_timePolicy = timePolicy;
  }

  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.TIMEPOLICY;
  }

}
