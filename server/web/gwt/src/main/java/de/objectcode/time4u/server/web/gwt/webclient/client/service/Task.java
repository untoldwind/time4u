package de.objectcode.time4u.server.web.gwt.webclient.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Task implements IsSerializable {
	private String id;
	private String projectId;
	private String name;

	public Task() {
	}

	public Task(String id, String projectId, String name) {
		this.id = id;
		this.projectId = projectId;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getName() {
		return name;
	}

}
