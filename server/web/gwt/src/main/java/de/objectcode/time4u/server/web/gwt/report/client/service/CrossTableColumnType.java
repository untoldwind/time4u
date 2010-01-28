package de.objectcode.time4u.server.web.gwt.report.client.service;

public enum CrossTableColumnType {
	PROJECT("Project"), TASK("Task");

	private String label;

	private CrossTableColumnType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
