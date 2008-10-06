package de.objectcode.time4u.migrator.server05.parts;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import de.objectcode.time4u.migrator.server05.old.entities.OldTasks;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

public class TaskMigratorPart implements IMigratorPart
{
  public void migrate(final SessionFactory oldSessionFactory, final SessionFactory newSessionFactory)
  {
    final Session oldSession = oldSessionFactory.openSession();
    final Session newSession = newSessionFactory.openSession();
    Transaction trx = null;

    try {
      trx = newSession.beginTransaction();

      final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(newSession);
      IRevisionLock revisionLock = revisionGenerator.getNextRevision(SynchronizableType.TASK, null);

      final Criteria criteria = oldSession.createCriteria(OldTasks.class);
      criteria.addOrder(Order.asc("id"));

      final ScrollableResults results = criteria.scroll(ScrollMode.FORWARD_ONLY);
      int count = 0;
      while (results.next()) {
        count++;
        final OldTasks oldTask = (OldTasks) results.get(0);
        final TaskEntity newTask = new TaskEntity(oldTask.getId().toString(), revisionLock.getLatestRevision(), 1L,
            oldTask.getProjectId() != null ? (ProjectEntity) newSession.get(ProjectEntity.class, oldTask.getProjectId()
                .toString()) : null, oldTask.getName());
        newTask.setDescription(oldTask.getDescription());
        newTask.setActive(oldTask.isActive());
        newTask.setDeleted(oldTask.isDeleted());

        newSession.merge(newTask);

        if (count % 10 == 0) {
          newSession.flush();
          trx.commit();
          trx = newSession.beginTransaction();
          revisionLock = revisionGenerator.getNextRevision(SynchronizableType.TASK, null);

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
