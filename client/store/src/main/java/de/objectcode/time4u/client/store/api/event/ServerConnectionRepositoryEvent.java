package de.objectcode.time4u.client.store.api.event;

import de.objectcode.time4u.server.api.data.ServerConnection;

public class ServerConnectionRepositoryEvent extends RepositoryEvent
{
  ServerConnection m_serverConnection;

  public ServerConnectionRepositoryEvent(final ServerConnection serverConnection)
  {
    m_serverConnection = serverConnection;
  }

  @Override
  public RepositoryEventType getEventType()
  {
    return RepositoryEventType.SERVER_CONNECTION;
  }

}
