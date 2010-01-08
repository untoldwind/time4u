package de.objectcode.time4u.server.web.gwt.main.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("task.service")
public interface TaskService extends RemoteService {
	List<Task> getTasks(String projectId);
}
