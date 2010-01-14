package de.objectcode.time4u.server.web.gwt.webclient.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface TaskServiceAsync {

	void getTasks(String projectId, AsyncCallback<List<Task>> callback);

}
