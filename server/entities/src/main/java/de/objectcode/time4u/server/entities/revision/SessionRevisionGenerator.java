package de.objectcode.time4u.server.entities.revision;

import java.util.UUID;

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
  public IRevisionLock getNextRevision(final EntityType entityType, final UUID part)
  {
    final RevisionEntityKey key = new RevisionEntityKey(entityType, part);

    final Query updateQuery = m_session
        .createSQLQuery("update T4U_REVISIONS set latestRevision = latestRevision + 1 where entityKeyValue=:entityKeyValue and clientPart=:clientPart and localPart=:localPart");
    updateQuery.setInteger("entityKeyValue", key.getEntityKeyValue());
    updateQuery.setLong("clientPart", key.getClientPart());
    updateQuery.setLong("localPart", key.getLocalPart());

    if (updateQuery.executeUpdate() != 1) {
      createInOwnTransaction(key);

      updateQuery.setInteger("entityKeyValue", key.getEntityKeyValue());
      updateQuery.setLong("clientPart", key.getClientPart());
      updateQuery.setLong("localPart", key.getLocalPart());

      if (updateQuery.executeUpdate() != 1) {
        throw new RuntimeException("Failed to get next revision number");
      }
    }

    final RevisionEntity revisionEntity = (RevisionEntity) m_session.get(RevisionEntity.class, new RevisionEntityKey(
        entityType, part));

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
