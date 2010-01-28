package de.objectcode.time4u.server.web.gwt.report.client.service;

public enum CrossTableRowType {
	TEAM("Team"), PERSON("Person");

	private String label;

	private CrossTableRowType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
