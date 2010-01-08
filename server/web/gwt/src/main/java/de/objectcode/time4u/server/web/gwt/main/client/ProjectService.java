package de.objectcode.time4u.server.web.gwt.main.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("project.service")
public interface ProjectService extends RemoteService{
	List<Project> getRootProjects();
	
	List<Project> getChildProjects(String projectId);
}
