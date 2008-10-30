package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.TeamSummary;

public class TeamRepositoryEvent extends RepositoryEvent
{
  final TeamSummary m_team;

  public TeamRepositoryEvent(final TeamSummary team)
  {
    m_team = team;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.TEAM;
  }

}