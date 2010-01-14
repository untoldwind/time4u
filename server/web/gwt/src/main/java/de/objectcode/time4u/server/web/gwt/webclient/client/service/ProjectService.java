package de.objectcode.time4u.server.web.gwt.webclient.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("project.service")
public interface ProjectService extends RemoteService {
	List<Project> getRootProjects();

	List<Project> getChildProjects(String projectId);

	void storeProject(Project project);
}
