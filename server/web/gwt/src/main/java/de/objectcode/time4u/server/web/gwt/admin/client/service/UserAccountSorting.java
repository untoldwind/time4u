package de.objectcode.time4u.server.web.gwt.admin.client.service;

public enum UserAccountSorting {
	USERID("id"),
	SURNAME("person.surname"),
	EMAIL("person.email"),
	LASTLOGIN("lastLogin");
	
	private final String column;

	private UserAccountSorting(String column) {
		this.column = column;
	}

	public String getColumn() {
		return column;
	}
}
