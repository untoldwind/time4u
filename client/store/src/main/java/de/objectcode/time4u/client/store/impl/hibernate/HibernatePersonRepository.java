package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.client.store.api.IPersonRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.PersonRepositoryEvent;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

/**
 * Hibernate implementation of the person repository interface.
 * 
 * @author junglas
 */
public class HibernatePersonRepository implements IPersonRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernatePersonRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public Person getPerson(final String personId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<Person>() {
      public Person perform(final Session session)
      {
        final PersonEntity personEntity = (PersonEntity) session.get(PersonEntity.class, personId);

        if (personEntity != null) {
          final Person person = new Person();
          personEntity.toDTO(person);

          return person;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public PersonSummary getPersonSummary(final String personId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<PersonSummary>() {
      public PersonSummary perform(final Session session)
      {
        final PersonEntity personEntity = (PersonEntity) session.get(PersonEntity.class, personId);

        if (personEntity != null) {
          final PersonSummary person = new PersonSummary();
          personEntity.toSummaryDTO(person);

          return person;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public List<Person> getPersons(final PersonFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<Person>>() {
      public List<Person> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(PersonEntity.class);

        if (filter.getDeleted() != null) {
          criteria.add(Restrictions.eq("deleted", filter.getDeleted()));
        }
        if (filter.getActive() != null) {
          if (filter.getActive()) {
            criteria.add(Restrictions.or(Restrictions.isNull("active"), Restrictions.eq("active", true)));
          } else {
            criteria.add(Restrictions.and(Restrictions.isNotNull("active"), Restrictions.eq("active", false)));
          }
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        if (filter.getMaxRevision() != null) {
          criteria.add(Restrictions.le("revision", filter.getMaxRevision()));
        }
        if (filter.getLastModifiedByClient() != null) {
          criteria.add(Restrictions.eq("lastModifiedByClient", filter.getLastModifiedByClient()));
        }
        if (filter.getMemberOfTeamId() != null) {
          criteria.createCriteria("memberOf").add(Restrictions.eq("id", filter.getMemberOfTeamId()));
        }
        switch (filter.getOrder()) {
          case ID:
            criteria.addOrder(Order.asc("id"));
            break;
          case NAME:
            criteria.addOrder(Order.asc("surname"));
            criteria.addOrder(Order.asc("givenName"));
            criteria.addOrder(Order.asc("id"));
            break;
        }

        final List<Person> result = new ArrayList<Person>();

        for (final Object row : criteria.list()) {
          final Person person = new Person();

          ((PersonEntity) row).toDTO(person);

          result.add(person);
        }

        return result;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public List<PersonSummary> getPersonSummaries(final PersonFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<PersonSummary>>() {
      public List<PersonSummary> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(PersonEntity.class);

        if (filter.getDeleted() != null) {
          criteria.add(Restrictions.eq("deleted", filter.getDeleted()));
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        if (filter.getMaxRevision() != null) {
          criteria.add(Restrictions.le("revision", filter.getMaxRevision()));
        }
        if (filter.getLastModifiedByClient() != null) {
          criteria.add(Restrictions.eq("lastModifiedByClient", filter.getLastModifiedByClient()));
        }
        if (filter.getMemberOfTeamId() != null) {
          criteria.createCriteria("memberOf").add(Restrictions.eq("id", filter.getMemberOfTeamId()));
        }
        switch (filter.getOrder()) {
          case ID:
            criteria.addOrder(Order.asc("id"));
            break;
          case NAME:
            criteria.addOrder(Order.asc("surname"));
            criteria.addOrder(Order.asc("givenName"));
            criteria.addOrder(Order.asc("id"));
            break;
        }

        final List<PersonSummary> result = new ArrayList<PersonSummary>();

        for (final Object row : criteria.list()) {
          final PersonSummary person = new PersonSummary();

          ((PersonEntity) row).toSummaryDTO(person);

          result.add(person);
        }

        return result;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public void storePerson(final Person person, final boolean modifiedByOwner) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation() {
      public void perform(final Session session)
      {
        final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);
        final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.PERSON, null);

        if (person.getId() == null) {
          person.setId(m_repository.generateLocalId(EntityType.PERSON));
        }
        final PersonEntity personEntity = new PersonEntity(person.getId(), revisionLock.getLatestRevision(),
            m_repository.getClientId());

        personEntity.fromDTO(person);
        if (modifiedByOwner) {
          personEntity.setLastModifiedByClient(m_repository.getClientId());
        }

        session.merge(personEntity);
        session.flush();

        personEntity.toDTO(person);
      }
    });

    m_repository.fireRepositoryEvent(new PersonRepositoryEvent(person));
  }
}
