package de.objectcode.time4u.client.connection.impl.ws.up;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.ws.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.ws.SynchronizationContext;
import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

public class SendProjectChangesCommand implements ISynchronizationCommand
{
  public boolean shouldRun(final SynchronizationContext context)
  {
    return context.getClientRevisionStatus(SynchronizableType.PROJECT) > context.getSynchronizationStatus(
        SynchronizableType.PROJECT).getLastSendRevision();
  }

  public void execute(final SynchronizationContext context) throws RepositoryException, ConnectionException
  {
    final IProjectRepository projectRepository = context.getRepository().getProjectRepository();

    final ProjectFilter filter = new ProjectFilter();
    filter.setLastModifiedByClient(context.getRepository().getClientId());

    // TODO: Chunk revisions
  }

}
