package de.objectcode.time4u.server.entities.revision;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
  public long getNextRevision(final EntityType entityType, final long part)
  {
    final RevisionEntityKey key = new RevisionEntityKey(entityType, part);
    RevisionEntity revisionEntity = (RevisionEntity) m_session.get(RevisionEntity.class, key, LockMode.UPGRADE);

    if (revisionEntity == null) {
      createInOwnTransaction(entityType, part);

      revisionEntity = (RevisionEntity) m_session.get(RevisionEntity.class, key, LockMode.UPGRADE);

      if (revisionEntity == null) {
        throw new RuntimeException("Failed to get next revision number");
      }
    }

    revisionEntity.setLatestRevision(revisionEntity.getLatestRevision() + 1);
    m_session.flush();

    return revisionEntity.getLatestRevision();
  }

  private void createInOwnTransaction(final EntityType entityType, final long part)
  {
    Transaction trx = null;
    Session session = null;

    try {
      session = m_session.getSessionFactory().openSession();
      trx = session.beginTransaction();

      final RevisionEntity revisionEntity = new RevisionEntity(entityType, part);

      m_session.persist(revisionEntity);
      m_session.flush();

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
