package de.objectcode.time4u.server.ejb.seam.api;

import java.util.List;

import de.objectcode.time4u.server.entities.ProjectEntity;

public interface IProjectServiceLocal
{
  ProjectEntity getProject(String projectId);

  List<ProjectEntity> getRootProjects();

  List<ProjectEntity> getChildProjects(String projectId);
}
