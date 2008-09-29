package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.ServerConnection;

public interface IServerConnectionRepository
{
  List<ServerConnection> getServerConnections() throws RepositoryException;

  ServerConnection storeServerConnection(ServerConnection serverConnection) throws RepositoryException;

  void deleteServerConnection(ServerConnection serverConnection) throws RepositoryException;
}
