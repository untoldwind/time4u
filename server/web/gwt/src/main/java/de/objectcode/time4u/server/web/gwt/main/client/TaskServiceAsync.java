package de.objectcode.time4u.server.web.gwt.main.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TaskServiceAsync {

	void getTasks(String projectId, AsyncCallback<List<Task>> callback);

}
