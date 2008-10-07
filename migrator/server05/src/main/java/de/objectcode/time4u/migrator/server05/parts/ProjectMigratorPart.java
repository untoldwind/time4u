package de.objectcode.time4u.migrator.server05.parts;

import org.hibernate.Session;

import de.objectcode.time4u.migrator.server05.old.entities.OldProjects;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

public class ProjectMigratorPart extends BaseMigratorPart<OldProjects>
{
  public ProjectMigratorPart()
  {
    super(SynchronizableType.PROJECT, OldProjects.class);
  }

  @Override
  protected void migrateEntity(final Session oldSession, final Session newSession, final OldProjects oldEntity,
      final IRevisionLock revisionLock)
  {
    final ProjectEntity newProject = new ProjectEntity(migrateId(SynchronizableType.PROJECT, oldEntity.getId()),
        revisionLock.getLatestRevision(), m_serverId, oldEntity.getName());
    newProject.setDescription(oldEntity.getDescription());
    newProject.setActive(oldEntity.isActive());
    newProject.setDeleted(oldEntity.isDeleted());
    if (oldEntity.getParentId() != null) {
      newProject.setParent((ProjectEntity) newSession.get(ProjectEntity.class, migrateId(SynchronizableType.PROJECT,
          oldEntity.getParentId())));
    }
    newProject.updateParentKey();

    newSession.merge(newProject);
  }
}
