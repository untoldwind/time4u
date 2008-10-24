package de.objectcode.time4u.client.connection.impl.common.down;

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
 * Base implementation of a receive changes from server command.
 * 
 * The synchronization mechanism is now extremely generic, so that most logic can be encapsulated in this base class.
 * 
 * (Or simply put: If you have a hammer, every problem becomes a nail)
 * 
 * @author junglas
 */
public abstract class BaseReceiveCommand<T extends ISynchronizableData> implements ISynchronizationCommand
{
  private final static int REVISION_CHUNK = 10;

  protected final EntityType m_entityType;

  protected BaseReceiveCommand(final EntityType entityType)
  {
    m_entityType = entityType;
  }

  /**
   * {@inheritDoc}
   */
  public boolean shouldRun(final SynchronizationContext context)
  {
    return context.getServerRevisionStatus(m_entityType) > context.getSynchronizationStatus(m_entityType)
        .getLastReceivedRevision();
  }

  /**
   * {@inheritDoc}
   */
  public void execute(final SynchronizationContext context, final IProgressMonitor monitor) throws RepositoryException,
      ConnectionException
  {
    final long clientId = context.getRepository().getClientId();
    final IServerConnectionRepository serverConnectionRepository = context.getRepository()
        .getServerConnectionRepository();

    final SynchronizationStatus status = context.getSynchronizationStatus(m_entityType);
    final long currentRemoteRevision = context.getServerRevisionStatus(m_entityType);

    monitor.beginTask("Receive " + m_entityType, (int) (currentRemoteRevision - status.getLastReceivedRevision()));
    monitor.subTask("Receive " + m_entityType);

    try {
      // Do in chunks to avoid extremely large result sets
      for (long beginRevision = status.getLastReceivedRevision() + 1; beginRevision <= currentRemoteRevision; beginRevision += REVISION_CHUNK) {
        if (monitor.isCanceled()) {
          break;
        }
        final long minRevision = beginRevision;
        final long maxRevision = min(beginRevision + REVISION_CHUNK, currentRemoteRevision);
        final List<T> entities = receiveEntities(context, minRevision, maxRevision);

        // Empty result might by null
        if (entities != null) {
          for (final T entity : entities) {
            // Ignore changes made by myself
            if (entity.getLastModifiedByClient() != clientId) {
              storeEntity(context, entity);
            }
          }
        }

        status.setLastReceivedRevision(maxRevision);
        serverConnectionRepository.storeSynchronizationStatus(context.getServerConnectionId(), status);

        monitor.worked((int) (maxRevision - minRevision));
      }
    } finally {
      monitor.done();
    }
  }

  protected abstract List<T> receiveEntities(SynchronizationContext context, long minRevision, long maxRevision)
      throws ConnectionException;

  protected abstract void storeEntity(SynchronizationContext context, T entity) throws RepositoryException;
}
