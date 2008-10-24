package de.objectcode.time4u.migrator.server05.parts;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.migrator.server05.old.entities.OldPersons;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

public abstract class BasePersonalizedMigratorPart<T> extends BaseMigratorPart<T>
{
  protected OldPersons m_oldPerson;
  protected PersonEntity m_newPerson;
  protected String m_personIdProperty;

  protected BasePersonalizedMigratorPart(final EntityType type, final Class<T> oldClass,
      final String personIdProperty)
  {
    super(type, oldClass);

    m_personIdProperty = personIdProperty;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void migrate(final ILocalIdGenerator idGenerator, final SessionFactory oldSessionFactory,
      final SessionFactory newSessionFactory)
  {
    m_idGenerator = idGenerator;

    final Session oldSession = oldSessionFactory.openSession();

    try {
      final Criteria personCriteria = oldSession.createCriteria(OldPersons.class);
      personCriteria.add(Restrictions.ne("userId", "admin"));

      final List<OldPersons> persons = personCriteria.list();

      for (final OldPersons person : persons) {
        m_oldPerson = person;

        System.out.println("Migrate " + m_type + " for " + m_oldPerson.getUserId());

        migratePerson(oldSession, newSessionFactory);
      }
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      if (oldSession != null) {
        oldSession.close();
      }
    }
  }

  @SuppressWarnings("unchecked")
  protected void migratePerson(final Session oldSession, final SessionFactory newSessionFactory)
  {
    final Session newSession = newSessionFactory.openSession();
    Transaction trx = null;

    try {
      trx = newSession.beginTransaction();

      m_newPerson = (PersonEntity) newSession.get(PersonEntity.class, migrateId(EntityType.PERSON, m_oldPerson
          .getId()));

      final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(newSession);
      IRevisionLock revisionLock = revisionGenerator.getNextRevision(m_type, m_newPerson.getId());

      final Criteria criteria = oldSession.createCriteria(m_oldClass);
      criteria.add(Restrictions.eq(m_personIdProperty, m_oldPerson.getId()));
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
          revisionLock = revisionGenerator.getNextRevision(m_type, m_newPerson.getId());

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
    }
  }
}
