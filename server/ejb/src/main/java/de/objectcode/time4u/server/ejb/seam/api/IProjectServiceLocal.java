package de.objectcode.time4u.server.ejb.seam.api;

import java.util.List;
import java.util.Map;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;

public interface IProjectServiceLocal
{
  ProjectEntity getProject(String projectId);

  List<ProjectEntity> getRootProjects(boolean deleted, boolean onlyActive);

  List<ProjectEntity> getChildProjects(String projectId, boolean deleted, boolean onlyActive);

  List<TaskEntity> getTasks(final String projectId);

  Map<PersonEntity, Long> checkTransferData(final String fromTaskId);

  void transferData(final List<String> personIds, final String fromTaskId, final String toTaskId);

  void deleteProject(String projectId);

  void undeleteProject(String projectId);
}
