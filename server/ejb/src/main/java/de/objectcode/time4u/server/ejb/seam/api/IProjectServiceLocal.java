package de.objectcode.time4u.server.ejb.seam.api;

import java.util.List;

import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;

public interface IProjectServiceLocal
{
  ProjectEntity getProject(String projectId);

  List<ProjectEntity> getRootProjects();

  List<ProjectEntity> getChildProjects(String projectId);

  List<TaskEntity> getTasks(final String projectId);
}
