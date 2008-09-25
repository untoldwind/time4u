package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.Project;

public class ProjectRepositoryEvent extends RepositoryEvent
{
  final Project m_project;

  public ProjectRepositoryEvent(final Project project)
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
