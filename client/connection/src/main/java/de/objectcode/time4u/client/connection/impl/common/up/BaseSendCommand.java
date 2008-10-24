package de.objectcode.time4u.client.connection.impl.common.up;

import static java.lang.Math.min;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.IServerConnectionRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.ISynchronizableData;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;

/**
 * Base implementation of a send changes to server command.
 * 
 * The synchronization mechanism is now extremely generic, so that most logic can be encapsulated in this base class.
 * 
 * (Or simply put: If you have a hammer, every problem becomes a nail)
 * 
 * @author junglas
 */
public abstract class BaseSendCommand<T extends ISynchronizableData> implements ISynchronizationCommand
{
  private final static int REVISION_CHUNK = 10;

  protected final EntityType m_entityType;

  protected BaseSendCommand(final EntityType entityType)
  {
    m_entityType = entityType;
  }

  /**
   * {@inheritDoc}
   */
  public boolean shouldRun(final SynchronizationContext context)
  {
    return context.getClientRevisionStatus(m_entityType) > context.getSynchronizationStatus(m_entityType)
        .getLastSendRevision();
  }

  /**
   * {@inheritDoc}
   */
  public void execute(final SynchronizationContext context, final IProgressMonitor monitor) throws RepositoryException,
      ConnectionException
  {
    final IServerConnectionRepository serverConnectionRepository = context.getRepository()
        .getServerConnectionRepository();

    final SynchronizationStatus status = context.getSynchronizationStatus(m_entityType);
    final long currentLocalRevision = context.getClientRevisionStatus(m_entityType);

    monitor.beginTask("Send " + m_entityType, (int) (currentLocalRevision - status.getLastSendRevision()));
    monitor.subTask("Send " + m_entityType);

    try {
      // Do in chunks to avoid extremely large result sets
      for (long beginRevision = status.getLastSendRevision() + 1; beginRevision <= currentLocalRevision; beginRevision += REVISION_CHUNK) {
        if (monitor.isCanceled()) {
          break;
        }
        final long minRevision = beginRevision;
        final long maxRevision = min(beginRevision + REVISION_CHUNK, currentLocalRevision);

        final List<T> entities = queryEntities(context, minRevision, maxRevision);

        // Empty result might by null
        if (entities != null) {
          for (final T entity : entities) {
            sendEntity(context, entity);
          }
        }

        status.setLastSendRevision(maxRevision);
        serverConnectionRepository.storeSynchronizationStatus(context.getServerConnectionId(), status);

        monitor.worked((int) (maxRevision - minRevision));
      }
    } finally {
      monitor.done();
    }
  }

  protected abstract List<T> queryEntities(SynchronizationContext context, long minRevision, long maxRevision)
      throws RepositoryException;

  protected abstract void sendEntity(SynchronizationContext context, T entity) throws ConnectionException;
}
