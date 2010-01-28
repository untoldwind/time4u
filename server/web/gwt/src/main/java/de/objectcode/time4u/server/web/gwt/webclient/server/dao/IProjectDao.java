package de.objectcode.time4u.server.web.gwt.webclient.server.dao;

import java.util.List;

import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Project;

public interface IProjectDao {
	List<Project> findRootProjectsDTO();

	List<Project> findChildProjectsDTO(String projectId);

	void storeProjectDTO(Project project);
	
	void save(ProjectEntity project);

	void update(ProjectEntity project);
}
