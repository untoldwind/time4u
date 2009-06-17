package de.objectcode.time4u.client.connection.impl.common.up;

import org.eclipse.core.runtime.IProgressMonitor;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;

public class SendSynchronizationStatusCommand implements ISynchronizationCommand
{

  public void execute(final SynchronizationContext context, final IProgressMonitor monitor) throws RepositoryException,
      ConnectionException
  {
    monitor.beginTask("Send synchronization status", 1);
    monitor.subTask("Send synchronization status");

    try {
      final IRevisionService revisionService = context.getRevisionService();

      revisionService.storeClientSynchronizationStatus(context.getRepository().getClientId(),
          new FilterResult<SynchronizationStatus>(context.getSynchronizationStatusList()));
    } finally {
      monitor.done();
    }
  }

  public boolean shouldRun(final SynchronizationContext context)
  {
    return true;
  }
}
