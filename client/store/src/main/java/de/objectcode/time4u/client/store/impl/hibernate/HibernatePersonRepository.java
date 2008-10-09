package de.objectcode.time4u.client.store.impl.hibernate;

import org.hibernate.Session;

import de.objectcode.time4u.client.store.api.IPersonRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.PersonRepositoryEvent;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Person;
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
  public Person storePerson(final Person person, final boolean modifiedByOwner) throws RepositoryException
  {
    final Person result = m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<Person>() {
      public Person perform(final Session session)
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

        final Person result = new Person();

        personEntity.toDTO(result);

        return result;
      }
    });

    m_repository.fireRepositoryEvent(new PersonRepositoryEvent(result));

    return result;
  }
}
