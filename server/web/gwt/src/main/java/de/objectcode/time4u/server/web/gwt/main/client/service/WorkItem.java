package de.objectcode.time4u.server.web.gwt.main.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WorkItem implements IsSerializable {
	private String id;
	private String projectId;
	private String taskId;
	private int begin;
	private int end;
	private String comment;
	private boolean valid;
	private String todoId;

	public WorkItem() {		
	}
	
	public WorkItem(String id, String projectId, String taskId, int begin,
			int end, String comment, boolean valid, String todoId) {
		this.id = id;
		this.projectId = projectId;
		this.taskId = taskId;
		this.begin = begin;
		this.end = end;
		this.comment = comment;
		this.valid = valid;
		this.todoId = todoId;
	}

	public String getId() {
		return id;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getTaskId() {
		return taskId;
	}

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}

	public String getComment() {
		return comment;
	}

	public boolean isValid() {
		return valid;
	}

	public String getTodoId() {
		return todoId;
	}

}
