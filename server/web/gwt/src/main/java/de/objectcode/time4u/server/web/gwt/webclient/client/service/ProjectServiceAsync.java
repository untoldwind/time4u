package de.objectcode.time4u.server.web.gwt.webclient.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProjectServiceAsync {
	void getRootProjects(AsyncCallback<List<Project>> callback);

	void getChildProjects(String projectId,
			AsyncCallback<List<Project>> callback);

	void storeProject(Project project, AsyncCallback<Void> callback);
}
