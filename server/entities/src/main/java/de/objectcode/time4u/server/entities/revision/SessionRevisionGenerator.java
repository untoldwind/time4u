package de.objectcode.time4u.server.entities.revision;

import org.hibernate.LockMode;
import org.hibernate.Session;

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
      revisionEntity = new RevisionEntity(entityType, part);

      try {
        m_session.persist(revisionEntity);
        m_session.flush();
      } catch (final Exception e) {
        // This might fail once
        revisionEntity = (RevisionEntity) m_session.get(RevisionEntity.class, key, LockMode.UPGRADE);
      }
    }

    revisionEntity.setLatestRevision(revisionEntity.getLatestRevision() + 1);
    m_session.flush();

    return revisionEntity.getLatestRevision();
  }
}
