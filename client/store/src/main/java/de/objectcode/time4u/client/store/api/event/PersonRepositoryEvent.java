package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.PersonSummary;

public class PersonRepositoryEvent extends RepositoryEvent
{
  final PersonSummary m_person;

  public PersonRepositoryEvent(final PersonSummary person)
  {
    m_person = person;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.PERSON;
  }

}
