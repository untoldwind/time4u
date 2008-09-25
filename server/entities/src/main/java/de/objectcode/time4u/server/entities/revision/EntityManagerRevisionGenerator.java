package de.objectcode.time4u.server.entities.revision;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public class EntityManagerRevisionGenerator implements IRevisionGenerator
{
  private final EntityManager m_entityManager;

  public EntityManagerRevisionGenerator(final EntityManager entityManager)
  {
    m_entityManager = entityManager;
  }

  /**
   * {@inheritDoc}
   */
  public long getNextRevision(final EntityType entityType, final long part)
  {
    final RevisionEntityKey key = new RevisionEntityKey(entityType, part);
    RevisionEntity revisionEntity = m_entityManager.find(RevisionEntity.class, key);

    if (revisionEntity == null) {
      revisionEntity = new RevisionEntity(entityType, part);

      try {
        m_entityManager.persist(revisionEntity);
        m_entityManager.flush();
      } catch (final Exception e) {
        // This might fail once
        revisionEntity = m_entityManager.find(RevisionEntity.class, key);
      }
    }

    m_entityManager.lock(revisionEntity, LockModeType.WRITE);
    m_entityManager.refresh(revisionEntity);

    revisionEntity.setLatestRevision(revisionEntity.getLatestRevision() + 1);
    m_entityManager.flush();

    return revisionEntity.getLatestRevision();
  }
}
