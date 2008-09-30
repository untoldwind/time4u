package de.objectcode.time4u.client.connection.impl.ws.down;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.ws.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.ws.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.SynchronizableType;

public class ReceiveProjectChangesCommand implements ISynchronizationCommand
{
  public boolean shouldRun(final SynchronizationContext context)
  {
    return context.getServerRevisionStatus(SynchronizableType.PROJECT) > context.getSynchronizationStatus(
        SynchronizableType.PROJECT).getLastReceivedRevision();
  }

  public void execute(final SynchronizationContext context) throws RepositoryException, ConnectionException
  {
    // TODO Auto-generated method stub

  }

}
