package de.objectcode.time4u.server.web.gwt.webclient.server.dao;

import java.util.List;

import de.objectcode.time4u.server.web.gwt.webclient.client.service.Task;

public interface ITaskDao {
	List<Task> findTasksDTO(String projectId);
}
