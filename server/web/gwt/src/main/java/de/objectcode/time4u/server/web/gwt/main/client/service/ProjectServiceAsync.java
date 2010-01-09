package de.objectcode.time4u.server.web.gwt.main.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProjectServiceAsync {
	void getRootProjects(AsyncCallback<List<Project>> callback);

	void getChildProjects(String projectId,
			AsyncCallback<List<Project>> callback);
}