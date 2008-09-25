package de.objectcode.time4u.client.store.impl.hibernate;

import java.io.File;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DerbyDialect;

import de.objectcode.time4u.client.store.StorePlugin;
import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.ProjectProperty;
import de.objectcode.time4u.server.entities.RoleEntity;
import de.objectcode.time4u.server.entities.TeamEntity;

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

  public HibernateRepository(final File directory)
  {
    m_hibernateTemplate = new HibernateTemplate(buildSessionFactory(directory));

    m_projectRepository = new HibernateProjectRepository(this, m_hibernateTemplate);
  }

  /**
   * {@inheritDoc}
   */
  public IProjectRepository getProjectRepository()
  {
    return m_projectRepository;
  }

  void fireRepositoryEvent(final RepositoryEvent event)
  {
    // TODO
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
      cfg.addAnnotatedClass(PersonEntity.class);
      cfg.addAnnotatedClass(RoleEntity.class);
      cfg.addAnnotatedClass(TeamEntity.class);
      cfg.addAnnotatedClass(ProjectEntity.class);
      cfg.addAnnotatedClass(ProjectProperty.class);

      return cfg.buildSessionFactory();
    } catch (final Exception e) {
      e.printStackTrace();
      StorePlugin.getDefault().log(e);
    }

    return null;
  }
}
