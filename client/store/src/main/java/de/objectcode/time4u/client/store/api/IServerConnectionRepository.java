package de.objectcode.time4u.client.store.api;

import java.util.List;
import java.util.Map;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.ServerConnection;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;

/**
 * Interface to the client side server connection repository.
 * 
 * This repository is mainly used by the connection plugin to store the server connection data and individual
 * synchronization stati.
 * 
 * @author junglas
 */
public interface IServerConnectionRepository
{
  List<ServerConnection> getServerConnections() throws RepositoryException;

  ServerConnection storeServerConnection(ServerConnection serverConnection) throws RepositoryException;

  void deleteServerConnection(ServerConnection serverConnection) throws RepositoryException;

  Map<EntityType, SynchronizationStatus> getSynchronizationStatus(long serverConnectionId) throws RepositoryException;

  void storeSynchronizationStatus(long serverConnectionId, SynchronizationStatus synchronizationStatus)
      throws RepositoryException;
}
