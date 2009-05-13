package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.client.store.api.IServerConnectionRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.ServerConnectionRepositoryEvent;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.ServerConnection;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;
import de.objectcode.time4u.server.entities.context.SessionPersistenceContext;
import de.objectcode.time4u.server.entities.sync.ServerConnectionEntity;
import de.objectcode.time4u.server.entities.sync.SynchronizationStatusEntity;

/**
 * Hibernate implementation of the server connection interface.
 * 
 * @author junglas
 */
public class HibernateServerConnectionRepository implements IServerConnectionRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateServerConnectionRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public List<ServerConnection> getServerConnections() throws RepositoryException
  {
    return m_hibernateTemplate
        .executeInTransaction(new HibernateTemplate.OperationWithResult<List<ServerConnection>>() {
          public List<ServerConnection> perform(final Session session)
          {
            final Criteria criteria = session.createCriteria(ServerConnectionEntity.class);
            criteria.addOrder(Order.asc("id"));

            final List<ServerConnection> result = new ArrayList<ServerConnection>();

            for (final Object row : criteria.list()) {
              final ServerConnection serverConnection = new ServerConnection();

              ((ServerConnectionEntity) row).toDTO(serverConnection, m_repository.getKeyChainEncoder());

              result.add(serverConnection);
            }

            return result;
          }

        });
  }

  /**
   * {@inheritDoc}
   */
  public ServerConnection storeServerConnection(final ServerConnection serverConnection) throws RepositoryException
  {
    final ServerConnection result = m_hibernateTemplate
        .executeInTransaction(new HibernateTemplate.OperationWithResult<ServerConnection>() {
          public ServerConnection perform(final Session session)
          {
            ServerConnectionEntity serverConnectionEntity;

            if (serverConnection.getId() > 0L) {
              serverConnectionEntity = (ServerConnectionEntity) session.get(ServerConnectionEntity.class,
                  serverConnection.getId());

              serverConnectionEntity.fromDTO(new SessionPersistenceContext(session), serverConnection, m_repository
                  .getKeyChainEncoder(), m_repository.getClientId());

              session.flush();
            } else {
              serverConnectionEntity = new ServerConnectionEntity();

              serverConnectionEntity.fromDTO(new SessionPersistenceContext(session), serverConnection, m_repository
                  .getKeyChainEncoder(), m_repository.getClientId());

              session.persist(serverConnectionEntity);
            }

            final ServerConnection result = new ServerConnection();

            serverConnectionEntity.toDTO(result, m_repository.getKeyChainEncoder());

            return result;
          }
        });

    m_repository.fireRepositoryEvent(new ServerConnectionRepositoryEvent(result));

    return result;
  }

  public void markSynchronized(final long serverConnectionId) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation() {
      public void perform(final Session session)
      {
        final ServerConnectionEntity serverConnectionEntity = (ServerConnectionEntity) session.get(
            ServerConnectionEntity.class, serverConnectionId);

        serverConnectionEntity.setLastSynchronize(new Date());

        session.flush();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public void deleteServerConnection(final ServerConnection serverConnection) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<Object>() {
      public Object perform(final Session session)
      {
        if (serverConnection.getId() >= 0) {
          final ServerConnectionEntity serverConnectionEntity = (ServerConnectionEntity) session.get(
              ServerConnectionEntity.class, serverConnection.getId());

          session.delete(serverConnectionEntity);
          session.flush();
        }

        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public Map<EntityType, SynchronizationStatus> getSynchronizationStatus(final long serverConnectionId)
      throws RepositoryException
  {
    return m_hibernateTemplate
        .executeInTransaction(new HibernateTemplate.OperationWithResult<Map<EntityType, SynchronizationStatus>>() {
          public Map<EntityType, SynchronizationStatus> perform(final Session session)
          {
            final Criteria criteria = session.createCriteria(SynchronizationStatusEntity.class);

            criteria.add(Restrictions.eq("serverConnection.id", serverConnectionId));

            final Map<EntityType, SynchronizationStatus> result = new HashMap<EntityType, SynchronizationStatus>();
            for (final Object row : criteria.list()) {
              final SynchronizationStatusEntity synchronizationStatusEntity = (SynchronizationStatusEntity) row;
              final SynchronizationStatus synchronizationStatus = new SynchronizationStatus();

              synchronizationStatusEntity.toDTO(synchronizationStatus);

              result.put(synchronizationStatusEntity.getEntityType(), synchronizationStatus);
            }

            for (final EntityType type : EntityType.values()) {
              if (!result.containsKey(type)) {
                final SynchronizationStatus synchronizationStatus = new SynchronizationStatus();
                synchronizationStatus.setEntityType(type);
                synchronizationStatus.setLastReceivedRevision(-1L);
                synchronizationStatus.setLastSendRevision(-1L);

                result.put(type, synchronizationStatus);
              }
            }

            return result;
          }
        });
  }

  /**
   * {@inheritDoc}
   */
  public void storeSynchronizationStatus(final long serverConnectionId,
      final SynchronizationStatus synchronizationStatus) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<Object>() {
      public Object perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(SynchronizationStatusEntity.class);

        criteria.add(Restrictions.eq("serverConnection.id", serverConnectionId));
        criteria.add(Restrictions.eq("entityType", synchronizationStatus.getEntityType()));

        SynchronizationStatusEntity synchronizationStatusEntity = (SynchronizationStatusEntity) criteria.uniqueResult();

        if (synchronizationStatusEntity == null) {
          synchronizationStatusEntity = new SynchronizationStatusEntity();
          synchronizationStatusEntity.setEntityType(synchronizationStatus.getEntityType());
          synchronizationStatusEntity.setServerConnection((ServerConnectionEntity) session.get(
              ServerConnectionEntity.class, serverConnectionId));

          synchronizationStatusEntity.fromDTO(synchronizationStatus);

          session.persist(synchronizationStatusEntity);
        } else {
          synchronizationStatusEntity.fromDTO(synchronizationStatus);
        }

        session.flush();

        return null;
      }
    });
  }

}