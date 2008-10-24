package de.objectcode.time4u.server.entities.revision;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import de.objectcode.time4u.server.api.data.EntityType;

public class SessionLocalIdGenerator implements ILocalIdGenerator
{
  public final static int CHUNK_SIZE = 100;

  SessionFactory m_sessionFactory;
  long m_clientId;
  Map<EntityType, EntityTypeIdGenerator> m_generators;

  public SessionLocalIdGenerator(final SessionFactory sessionFactory, final long clientId)
  {
    m_sessionFactory = sessionFactory;
    m_clientId = clientId;
    final Map<EntityType, EntityTypeIdGenerator> generators = new HashMap<EntityType, EntityTypeIdGenerator>();

    for (final EntityType type : EntityType.values()) {
      generators.put(type, new EntityTypeIdGenerator(type));
    }
    m_generators = Collections.unmodifiableMap(generators);

    Transaction trx = null;
    Session session = null;

    try {
      session = m_sessionFactory.openSession();
      trx = session.beginTransaction();

      for (final EntityType type : EntityType.values()) {
        if (session.get(LocalIdEntity.class, type) == null) {
          session.persist(new LocalIdEntity(type));
        }
      }
      session.flush();
      trx.commit();
    } finally {
      if (trx != null && trx.isActive()) {
        trx.rollback();
      }
      if (session != null) {
        session.close();
      }
    }

  }

  public long getClientId()
  {
    return m_clientId;
  }

  public String generateLocalId(final EntityType entityType)
  {
    return m_generators.get(entityType).generateLocalId();
  }

  private static String digits(final long val, final int digits)
  {
    final long hi = 1L << digits * 4;
    return Long.toHexString(hi | val & hi - 1).substring(1);
  }

  private LocalIdEntity getNextChunk(final EntityType entityType)
  {
    Transaction trx = null;
    Session session = null;

    try {
      session = m_sessionFactory.openSession();
      trx = session.beginTransaction();

      final Query updateQuery = session.createSQLQuery("update T4U_LOCALID set loId = hiId + 1, hiId = hiId + "
          + CHUNK_SIZE + " where entityType=:entityType");
      updateQuery.setInteger("entityType", entityType.getCode());

      if (updateQuery.executeUpdate() != 1) {
        throw new RuntimeException("Failed to get next localId");
      }

      final LocalIdEntity localIdEntity = (LocalIdEntity) session.get(LocalIdEntity.class, entityType);

      if (localIdEntity == null) {
        throw new RuntimeException("Failed to get next localId");
      }

      trx.commit();

      return localIdEntity;
    } finally {
      if (trx != null && trx.isActive()) {
        trx.rollback();
      }
      if (session != null) {
        session.close();
      }
    }
  }

  private class EntityTypeIdGenerator
  {
    EntityType m_entityType;
    long m_nextLocalId;
    LocalIdEntity m_current;

    EntityTypeIdGenerator(final EntityType entityType)
    {
      m_entityType = entityType;
    }

    public synchronized String generateLocalId()
    {
      if (m_current == null || m_nextLocalId > m_current.getHiId()) {
        final LocalIdEntity localIdEntity = getNextChunk(m_entityType);

        m_current = localIdEntity;
        m_nextLocalId = localIdEntity.getLoId();
      }
      final long localId = m_nextLocalId++;
      final StringBuffer buffer = new StringBuffer();

      buffer.append(digits(m_clientId >> 32, 8));
      buffer.append(digits(m_clientId, 8));
      buffer.append('-');
      buffer.append(digits(m_entityType.getCode(), 2));
      buffer.append('-');
      buffer.append(digits(localId, 14));

      return buffer.toString();
    }
  }
}
