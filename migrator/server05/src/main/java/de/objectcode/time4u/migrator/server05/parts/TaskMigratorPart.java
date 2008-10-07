package de.objectcode.time4u.migrator.server05.parts;

import org.hibernate.Session;

import de.objectcode.time4u.migrator.server05.old.entities.OldTasks;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

public class TaskMigratorPart extends BaseMigratorPart<OldTasks>
{
  public TaskMigratorPart()
  {
    super(SynchronizableType.TASK, OldTasks.class);
  }

  @Override
  protected void migrateEntity(final Session oldSession, final Session newSession, final OldTasks oldEntity,
      final IRevisionLock revisionLock)
  {
    final TaskEntity newTask = new TaskEntity(migrateId(SynchronizableType.TASK, oldEntity.getId()), revisionLock
        .getLatestRevision(), m_serverId, oldEntity.getProjectId() != null ? (ProjectEntity) newSession.get(
        ProjectEntity.class, migrateId(SynchronizableType.PROJECT, oldEntity.getProjectId())) : null, oldEntity
        .getName());
    newTask.setDescription(oldEntity.getDescription());
    newTask.setActive(oldEntity.isActive());
    newTask.setDeleted(oldEntity.isDeleted());

    newSession.merge(newTask);
  }
}
