package de.objectcode.time4u.migrator.server05.parts;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

public abstract class BaseMigratorPart<T> implements IMigratorPart
{
  protected EntityType m_type;
  protected Class<T> m_oldClass;

  protected ILocalIdGenerator m_idGenerator;

  protected BaseMigratorPart(final EntityType type, final Class<T> oldClass)
  {
    m_type = type;
    m_oldClass = oldClass;
  }

  @SuppressWarnings("unchecked")
  public void migrate(final ILocalIdGenerator idGenerator, final SessionFactory oldSessionFactory,
      final SessionFactory newSessionFactory)
  {
    m_idGenerator = idGenerator;

    final Session oldSession = oldSessionFactory.openSession();
    final Session newSession = newSessionFactory.openSession();
    Transaction trx = null;

    try {
      trx = newSession.beginTransaction();

      final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(newSession);
      IRevisionLock revisionLock = revisionGenerator.getNextRevision(m_type, null);

      final Criteria criteria = oldSession.createCriteria(m_oldClass);
      criteria.addOrder(Order.asc("id"));

      final ScrollableResults results = criteria.scroll(ScrollMode.FORWARD_ONLY);
      int count = 0;
      System.out.println("Migrate " + m_type + " start");
      while (results.next()) {
        count++;
        final T oldEntity = (T) results.get(0);

        migrateEntity(oldSession, newSession, oldEntity, revisionLock);

        if (count % 10 == 0) {
          newSession.flush();
          trx.commit();
          newSession.clear();
          trx = newSession.beginTransaction();
          revisionLock = revisionGenerator.getNextRevision(m_type, null);

          oldSession.clear();
        }
        if (count % 100 == 0) {
          System.out.println("Migrate " + m_type + " " + count);
        }
      }
      System.out.println("Migrate " + m_type + " " + count + " done");

      trx.commit();
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      if (trx != null && trx.isActive()) {
        trx.rollback();
      }
      newSession.close();
      oldSession.close();
    }
  }

  protected String migrateId(final EntityType type, final long oldId)
  {
    final StringBuffer buffer = new StringBuffer();
    if ((oldId & 0xffffffffffffffL) != oldId) {
      System.err.println("Old id too big: " + oldId);

      buffer.append(digits(oldId >> 32, 8));
      buffer.append(digits(oldId, 8));

      return buffer.toString();
    }

    buffer.append(digits(m_idGenerator.getClientId() >> 32, 8));
    buffer.append(digits(m_idGenerator.getClientId(), 8));
    buffer.append('-');
    buffer.append(digits(type.getCode() | 0x80, 2));
    buffer.append('-');
    buffer.append(digits(oldId, 14));

    return buffer.toString();
  }

  private static String digits(final long val, final int digits)
  {
    final long hi = 1L << digits * 4;
    return Long.toHexString(hi | val & hi - 1).substring(1);
  }

  protected abstract void migrateEntity(Session oldSession, Session newSession, T oldEntity, IRevisionLock revisionLock);
}
