package de.objectcode.time4u.server.web.gwt.server.dao;

import java.util.List;

import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.web.gwt.client.Project;

public interface IProjectDao {
	List<Project> findRootProjectsDTO();

	void save(ProjectEntity project);

	void update(ProjectEntity project);
}
