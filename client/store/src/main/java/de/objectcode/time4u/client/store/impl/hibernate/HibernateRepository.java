package de.objectcode.time4u.client.store.impl.hibernate;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DerbyDialect;

import de.objectcode.time4u.client.store.StorePlugin;
import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.store.impl.hibernate.entities.ClientDataEntity;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.entities.ActiveWorkItemEntity;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.DayTagEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.ProjectProperty;
import de.objectcode.time4u.server.entities.RoleEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TaskProperty;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.TodoProperty;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.revision.EntityType;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.RevisionEntity;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

/**
 * Hibernate implementation of the repository interface.
 * 
 * For convenience we are using lots of server entities. There is no reason to redefine the whole data model here.
 * 
 * @author junglas
 */
public class HibernateRepository implements IRepository
{
  private final HibernateTemplate m_hibernateTemplate;

  private Person m_owner;
  private long m_clientId;

  private final HibernateProjectRepository m_projectRepository;
  private final HibernateTaskRepository m_taskRepository;
  private final HibernateWorkItemRepository m_workItemRepository;

  private final Map<RepositoryEventType, List<IRepositoryListener>> m_listeners = new HashMap<RepositoryEventType, List<IRepositoryListener>>();

  public HibernateRepository(final File directory) throws RepositoryException
  {
    m_hibernateTemplate = new HibernateTemplate(buildSessionFactory(directory));

    initialize();
    m_projectRepository = new HibernateProjectRepository(this, m_hibernateTemplate);
    m_taskRepository = new HibernateTaskRepository(this, m_hibernateTemplate);
    m_workItemRepository = new HibernateWorkItemRepository(this, m_hibernateTemplate);
  }

  /**
   * {@inheritDoc}
   */
  public IProjectRepository getProjectRepository()
  {
    return m_projectRepository;
  }

  /**
   * {@inheritDoc}
   */
  public ITaskRepository getTaskRepository()
  {
    return m_taskRepository;
  }

  /**
   * {@inheritDoc}
   */
  public HibernateWorkItemRepository getWorkItemRepository()
  {
    return m_workItemRepository;
  }

  /**
   * {@inheritDoc}
   */
  public void addRepositoryListener(final RepositoryEventType eventType, final IRepositoryListener listener)
  {
    synchronized (m_listeners) {
      List<IRepositoryListener> listeners = m_listeners.get(eventType);

      if (listeners == null) {
        listeners = new ArrayList<IRepositoryListener>();

        m_listeners.put(eventType, listeners);
      }
      listeners.add(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void removeRepositoryListener(final RepositoryEventType eventType, final IRepositoryListener listener)
  {
    synchronized (m_listeners) {
      final List<IRepositoryListener> listeners = m_listeners.get(eventType);

      if (listeners != null) {
        listeners.remove(listener);
      }
    }
  }

  Person getOwner()
  {
    return m_owner;
  }

  long getClientId()
  {
    return m_clientId;
  }

  void fireRepositoryEvent(final RepositoryEvent event)
  {
    IRepositoryListener[] listenerArray;

    synchronized (m_listeners) {
      final List<IRepositoryListener> listeners = m_listeners.get(event.getEventType());

      if (listeners == null) {
        return;
      }

      listenerArray = listeners.toArray(new IRepositoryListener[listeners.size()]);
    }

    for (final IRepositoryListener listener : listenerArray) {
      listener.handleRepositoryEvent(event);
    }
  }

  private void initialize() throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<Object>() {
      public Object perform(final Session session)
      {
        ClientDataEntity clientData = (ClientDataEntity) session.get(ClientDataEntity.class, 1);

        if (clientData == null) {
          // TODO: Reconsider this adhoc generation
          final long clientId = new SecureRandom().nextLong();
          final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);
          final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.PERSON, null);
          final String personId = revisionLock.generateId(clientId);
          final PersonEntity ownerPerson = new PersonEntity(personId, System.getProperty("user.name"));
          ownerPerson.setName(System.getProperty("user.name"));

          session.persist(ownerPerson);

          clientData = new ClientDataEntity();
          clientData.setId(1);
          clientData.setOwnerPerson(ownerPerson);
          clientData.setClientId(clientId);

          session.persist(clientData);

          session.flush();
        }

        m_owner = new Person();
        clientData.getOwnerPerson().toDTO(m_owner);
        m_clientId = clientData.getClientId();

        return null;
      }
    });
  }

  private SessionFactory buildSessionFactory(final File directory)
  {
    try {
      System.setProperty("derby.system.home", directory.getAbsolutePath());

      final AnnotationConfiguration cfg = new AnnotationConfiguration();

      cfg.setProperty(Environment.DRIVER, EmbeddedDriver.class.getName());
      cfg.setProperty(Environment.URL, "jdbc:derby:" + directory.getAbsolutePath() + "/time4u;create=true");
      cfg.setProperty(Environment.USER, "sa");
      cfg.setProperty(Environment.PASS, "");
      cfg.setProperty(Environment.POOL_SIZE, "5");
      cfg.setProperty(Environment.AUTOCOMMIT, "false");
      cfg.setProperty(Environment.ISOLATION, "2");
      cfg.setProperty(Environment.DIALECT, DerbyDialect.class.getName());
      cfg.setProperty(Environment.HBM2DDL_AUTO, "update");
      cfg.setProperty(Environment.SHOW_SQL, "false");

      cfg.addAnnotatedClass(RevisionEntity.class);
      cfg.addAnnotatedClass(PersonEntity.class);
      cfg.addAnnotatedClass(RoleEntity.class);
      cfg.addAnnotatedClass(TeamEntity.class);
      cfg.addAnnotatedClass(ProjectEntity.class);
      cfg.addAnnotatedClass(ProjectProperty.class);
      cfg.addAnnotatedClass(TaskEntity.class);
      cfg.addAnnotatedClass(TaskProperty.class);
      cfg.addAnnotatedClass(DayInfoEntity.class);
      cfg.addAnnotatedClass(DayTagEntity.class);
      cfg.addAnnotatedClass(WorkItemEntity.class);
      cfg.addAnnotatedClass(TodoEntity.class);
      cfg.addAnnotatedClass(TodoProperty.class);
      cfg.addAnnotatedClass(ClientDataEntity.class);
      cfg.addAnnotatedClass(ActiveWorkItemEntity.class);

      return cfg.buildSessionFactory();
    } catch (final Exception e) {
      e.printStackTrace();
      StorePlugin.getDefault().log(e);
    }

    return null;
  }
}
