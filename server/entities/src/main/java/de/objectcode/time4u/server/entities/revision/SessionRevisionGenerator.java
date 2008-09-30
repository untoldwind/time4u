package de.objectcode.time4u.server.entities.revision;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.objectcode.time4u.server.api.data.SynchronizableType;

public class SessionRevisionGenerator implements IRevisionGenerator
{
  private final Session m_session;

  public SessionRevisionGenerator(final Session session)
  {
    m_session = session;
  }

  /**
   * {@inheritDoc}
   */
  public IRevisionLock getNextRevision(final SynchronizableType entityType, final String part)
  {
    final RevisionEntityKey key = new RevisionEntityKey(entityType, part != null ? part : "<default>");

    final Query updateQuery = m_session
        .createSQLQuery("update T4U_REVISIONS set latestRevision = latestRevision + 1 where entityType=:entityType and part=:part");
    updateQuery.setInteger("entityType", key.getEntityType().getValue());
    updateQuery.setString("part", key.getPart());

    if (updateQuery.executeUpdate() != 1) {
      createInOwnTransaction(key);

      updateQuery.setInteger("entityType", key.getEntityType().getValue());
      updateQuery.setString("part", key.getPart());

      if (updateQuery.executeUpdate() != 1) {
        throw new RuntimeException("Failed to get next revision number");
      }
    }

    final RevisionEntity revisionEntity = (RevisionEntity) m_session.get(RevisionEntity.class, key);

    return revisionEntity;
  }

  private void createInOwnTransaction(final RevisionEntityKey key)
  {
    Transaction trx = null;
    Session session = null;

    try {
      session = m_session.getSessionFactory().openSession();
      trx = session.beginTransaction();

      final RevisionEntity revisionEntity = new RevisionEntity(key);

      session.persist(revisionEntity);
      session.flush();

      trx.commit();
    } catch (final Exception e) {
      // This might fail for multiple threads
    } finally {
      if (trx != null && trx.isActive()) {
        trx.rollback();
      }
      if (session != null) {
        session.close();
      }
    }
  }
}
