package de.objectcode.time4u.migrator.server05;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import de.objectcode.time4u.migrator.server05.old.entities.OldProjects;
import de.objectcode.time4u.migrator.server05.old.entities.OldTasks;
import de.objectcode.time4u.migrator.server05.parts.IMigratorPart;
import de.objectcode.time4u.migrator.server05.parts.ProjectMigratorPart;
import de.objectcode.time4u.migrator.server05.parts.TaskMigratorPart;
import de.objectcode.time4u.server.entities.ActiveWorkItemEntity;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.DayTagEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.ProjectProperty;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TaskProperty;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.TodoProperty;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.revision.RevisionEntity;
import de.objectcode.time4u.server.entities.sync.ServerConnectionEntity;
import de.objectcode.time4u.server.entities.sync.SynchronizationStatusEntity;

public class Migrator
{
  SessionFactory m_oldSessionFactory;
  SessionFactory m_newSessionFactory;

  List<IMigratorPart> m_migratorParts;

  Migrator()
  {
    m_oldSessionFactory = buildOldSessionFactory();
    m_newSessionFactory = buildNewSessionFactory();

    m_migratorParts = new ArrayList<IMigratorPart>();
    m_migratorParts.add(new ProjectMigratorPart());
    m_migratorParts.add(new TaskMigratorPart());
  }

  private SessionFactory buildNewSessionFactory()
  {
    final AnnotationConfiguration cfg = new AnnotationConfiguration();

    cfg.configure("new-version.cfg.xml");

    cfg.addAnnotatedClass(RevisionEntity.class);
    cfg.addAnnotatedClass(PersonEntity.class);
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
    cfg.addAnnotatedClass(ActiveWorkItemEntity.class);
    cfg.addAnnotatedClass(ServerConnectionEntity.class);
    cfg.addAnnotatedClass(SynchronizationStatusEntity.class);

    return cfg.buildSessionFactory();
  }

  private SessionFactory buildOldSessionFactory()
  {
    final AnnotationConfiguration cfg = new AnnotationConfiguration();

    cfg.configure("old-version.cfg.xml");

    cfg.addAnnotatedClass(OldProjects.class);
    cfg.addAnnotatedClass(OldTasks.class);

    return cfg.buildSessionFactory();
  }

  public void run()
  {
    for (final IMigratorPart part : m_migratorParts) {
      part.migrate(m_oldSessionFactory, m_newSessionFactory);
    }
  }

  public static void main(final String args[])
  {
    final Migrator migrator = new Migrator();

    migrator.run();
  }
}
