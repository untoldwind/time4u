package de.objectcode.time4u.client.store.impl.hibernate;

import java.io.File;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DerbyDialect;

import de.objectcode.time4u.client.store.StorePlugin;
import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.server.entities.PersonEntity;
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
  private final SessionFactory sessionFactory;

  public HibernateRepository(final File directory)
  {
    sessionFactory = buildSessionFactory(directory);
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

      return cfg.buildSessionFactory();
    } catch (final Exception e) {
      e.printStackTrace();
      StorePlugin.getDefault().log(e);
    }

    return null;
  }
}
