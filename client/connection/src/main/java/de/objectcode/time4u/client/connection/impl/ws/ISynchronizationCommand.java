package de.objectcode.time4u.client.connection.impl.ws;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.store.api.RepositoryException;

public interface ISynchronizationCommand
{
  boolean shouldRun(SynchronizationContext context);

  void execute(SynchronizationContext context) throws RepositoryException, ConnectionException;
}
