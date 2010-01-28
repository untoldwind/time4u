package de.objectcode.time4u.server.web.gwt.webclient.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WorkItem implements IsSerializable {
	private String id;
	private Project project;
	private Task task;
	private int begin;
	private int end;
	private String comment;
	private boolean valid;
	private String todoId;

	public WorkItem() {		
	}
	
	public WorkItem(String id, Project project, Task task, int begin,
			int end, String comment, boolean valid, String todoId) {
		this.id = id;
		this.project = project;
		this.task = task;
		this.begin = begin;
		this.end = end;
		this.comment = comment;
		this.valid = valid;
		this.todoId = todoId;
	}

	public String getId() {
		return id;
	}

	public Project getProject() {
		return project;
	}

	public Task getTask() {
		return task;
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
