package de.objectcode.time4u.migrator.server05.parts;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import de.objectcode.time4u.migrator.server05.old.entities.OldProjects;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

public class ProjectMigratorPart implements IMigratorPart
{
  public void migrate(final SessionFactory oldSessionFactory, final SessionFactory newSessionFactory)
  {
    final Session oldSession = oldSessionFactory.openSession();
    final Session newSession = newSessionFactory.openSession();
    Transaction trx = null;

    try {
      trx = newSession.beginTransaction();

      final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(newSession);
      IRevisionLock revisionLock = revisionGenerator.getNextRevision(SynchronizableType.PROJECT, null);

      final Criteria criteria = oldSession.createCriteria(OldProjects.class);
      criteria.addOrder(Order.asc("id"));

      final ScrollableResults results = criteria.scroll(ScrollMode.FORWARD_ONLY);
      int count = 0;
      while (results.next()) {
        count++;
        final OldProjects oldProject = (OldProjects) results.get(0);
        final ProjectEntity newProject = new ProjectEntity(oldProject.getId().toString(), revisionLock
            .getLatestRevision(), 1L, oldProject.getName());
        newProject.setDescription(oldProject.getDescription());
        newProject.setActive(oldProject.isActive());
        newProject.setDeleted(oldProject.isDeleted());
        if (oldProject.getParentId() != null) {
          newProject
              .setParent((ProjectEntity) newSession.get(ProjectEntity.class, oldProject.getParentId().toString()));
        }
        newProject.updateParentKey();

        newSession.merge(newProject);

        if (count % 10 == 0) {
          newSession.flush();
          trx.commit();
          trx = newSession.beginTransaction();
          revisionLock = revisionGenerator.getNextRevision(SynchronizableType.PROJECT, null);

          newSession.clear();
          oldSession.clear();
        }
      }

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
}
