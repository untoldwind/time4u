package de.objectcode.time4u.server.entities.test;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DerbyDialect;

import de.objectcode.time4u.server.entities.ActiveWorkItemEntity;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.DayInfoMetaPropertyEntity;
import de.objectcode.time4u.server.entities.DayTagEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.PersonMetaPropertyEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.ProjectMetaPropertyEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TaskMetaPropertyEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TeamMetaPropertyEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.TodoMetaPropertyEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;
import de.objectcode.time4u.server.entities.revision.LocalIdEntity;
import de.objectcode.time4u.server.entities.revision.RevisionEntity;

public class TestSessionFactory
{
  private static SessionFactory sessionFactory;

  public static synchronized SessionFactory getInstance() throws Exception
  {
    if (sessionFactory == null) {
      sessionFactory = buildSessionFactory();
    }
    return sessionFactory;
  }

  private static SessionFactory buildSessionFactory() throws Exception
  {
    System.setProperty("derby.system.home", "./target/test");

    final AnnotationConfiguration cfg = new AnnotationConfiguration();

    cfg.setProperty(Environment.DRIVER, EmbeddedDriver.class.getName());
    cfg.setProperty(Environment.URL, "jdbc:derby:./target/test/time4u;create=true");
    cfg.setProperty(Environment.USER, "sa");
    cfg.setProperty(Environment.PASS, "");
    cfg.setProperty(Environment.POOL_SIZE, "5");
    cfg.setProperty(Environment.AUTOCOMMIT, "false");
    cfg.setProperty(Environment.ISOLATION, "2");
    cfg.setProperty(Environment.DIALECT, DerbyDialect.class.getName());
    cfg.setProperty(Environment.HBM2DDL_AUTO, "update");
    cfg.setProperty(Environment.SHOW_SQL, "false");

    cfg.addAnnotatedClass(RevisionEntity.class);
    cfg.addAnnotatedClass(LocalIdEntity.class);
    cfg.addAnnotatedClass(PersonEntity.class);
    cfg.addAnnotatedClass(PersonMetaPropertyEntity.class);
    cfg.addAnnotatedClass(UserRoleEntity.class);
    cfg.addAnnotatedClass(TeamEntity.class);
    cfg.addAnnotatedClass(TeamMetaPropertyEntity.class);
    cfg.addAnnotatedClass(ProjectEntity.class);
    cfg.addAnnotatedClass(ProjectMetaPropertyEntity.class);
    cfg.addAnnotatedClass(TaskEntity.class);
    cfg.addAnnotatedClass(TaskMetaPropertyEntity.class);
    cfg.addAnnotatedClass(DayInfoEntity.class);
    cfg.addAnnotatedClass(DayTagEntity.class);
    cfg.addAnnotatedClass(DayInfoMetaPropertyEntity.class);
    cfg.addAnnotatedClass(WorkItemEntity.class);
    cfg.addAnnotatedClass(TodoEntity.class);
    cfg.addAnnotatedClass(TodoMetaPropertyEntity.class);
    cfg.addAnnotatedClass(ActiveWorkItemEntity.class);
    cfg.addAnnotatedClass(TimePolicyEntity.class);

    return cfg.buildSessionFactory();
  }
}
