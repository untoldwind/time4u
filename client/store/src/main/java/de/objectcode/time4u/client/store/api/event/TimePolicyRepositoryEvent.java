package de.objectcode.time4u.client.store.api.event;

public class TimePolicyRepositoryEvent extends RepositoryEvent
{

  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.TIMEPOLICY;
  }

}
