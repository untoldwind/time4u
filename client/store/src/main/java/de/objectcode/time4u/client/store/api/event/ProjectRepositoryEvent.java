package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.ProjectSummary;

public class ProjectRepositoryEvent extends RepositoryEvent
{
  final ProjectSummary m_project;

  public ProjectRepositoryEvent(final ProjectSummary project)
  {
    super();
    m_project = project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.PROJECT;
  }
}
