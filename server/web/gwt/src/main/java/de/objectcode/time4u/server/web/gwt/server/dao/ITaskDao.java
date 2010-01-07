package de.objectcode.time4u.server.web.gwt.server.dao;

import java.util.List;

import de.objectcode.time4u.server.web.gwt.client.Task;

public interface ITaskDao {
	List<Task> findTasksDTO(String projectId);
}
