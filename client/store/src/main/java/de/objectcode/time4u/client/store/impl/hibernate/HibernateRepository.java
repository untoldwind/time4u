package de.objectcode.time4u.client.store.impl.hibernate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DerbyDialect;

import de.objectcode.time4u.client.store.StorePlugin;
import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
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
import de.objectcode.time4u.server.entities.revision.RevisionEntity;

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

  private final HibernateProjectRepository m_projectRepository;
  private final HibernateTaskRepository m_taskRepository;

  private final Map<RepositoryEventType, List<IRepositoryListener>> m_listeners = new HashMap<RepositoryEventType, List<IRepositoryListener>>();

  public HibernateRepository(final File directory)
  {
    m_hibernateTemplate = new HibernateTemplate(buildSessionFactory(directory));

    m_projectRepository = new HibernateProjectRepository(this, m_hibernateTemplate);
    m_taskRepository = new HibernateTaskRepository(this, m_hibernateTemplate);
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
      cfg.setProperty(Environment.SHOW_SQL, "true");

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

      return cfg.buildSessionFactory();
    } catch (final Exception e) {
      e.printStackTrace();
      StorePlugin.getDefault().log(e);
    }

    return null;
  }
}
