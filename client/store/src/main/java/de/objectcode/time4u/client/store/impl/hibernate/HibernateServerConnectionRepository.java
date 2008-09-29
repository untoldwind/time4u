package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import de.objectcode.time4u.client.store.api.IServerConnectionRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.ServerConnection;
import de.objectcode.time4u.server.entities.context.SessionPersistenceContext;
import de.objectcode.time4u.server.entities.sync.ServerConnectionEntity;

public class HibernateServerConnectionRepository implements IServerConnectionRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateServerConnectionRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  public List<ServerConnection> getServerConnections() throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<List<ServerConnection>>() {
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

  public ServerConnection storeServerConnection(final ServerConnection serverConnection) throws RepositoryException
  {
    final ServerConnection result = m_hibernateTemplate
        .executeInTransaction(new HibernateTemplate.Operation<ServerConnection>() {
          public ServerConnection perform(final Session session)
          {
            ServerConnectionEntity serverConnectionEntity;

            if (serverConnection.getId() >= 0) {
              serverConnectionEntity = (ServerConnectionEntity) session.get(ServerConnectionEntity.class,
                  serverConnection.getId());

              serverConnectionEntity.fromDTO(new SessionPersistenceContext(session), serverConnection, m_repository
                  .getKeyChainEncoder());

              session.flush();
            } else {
              serverConnectionEntity = new ServerConnectionEntity();

              serverConnectionEntity.fromDTO(new SessionPersistenceContext(session), serverConnection, m_repository
                  .getKeyChainEncoder());

              session.persist(serverConnectionEntity);
            }

            final ServerConnection result = new ServerConnection();

            serverConnectionEntity.toDTO(result, m_repository.getKeyChainEncoder());

            return result;
          }
        });

    return result;
  }

  public void deleteServerConnection(final ServerConnection serverConnection) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<Object>() {
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

}