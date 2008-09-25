package de.objectcode.time4u.server.entities.revision;

import org.hibernate.Query;
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
    final Query updateQuery = m_session
        .createSQLQuery("update T4U_REVISIONS set latestRevision = latestRevision + 1 where entityKeyValue=:entityKeyValue and part=:part");
    updateQuery.setInteger("entityKeyValue", entityType.getValue());
    updateQuery.setLong("part", part);

    if (updateQuery.executeUpdate() != 1) {
      createInOwnTransaction(entityType, part);

      updateQuery.setInteger("entityKeyValue", entityType.getValue());
      updateQuery.setLong("part", part);

      if (updateQuery.executeUpdate() != 1) {
        throw new RuntimeException("Failed to get next revision number");
      }
    }

    final RevisionEntity revisionEntity = (RevisionEntity) m_session.get(RevisionEntity.class, new RevisionEntityKey(
        entityType, part));

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
