package de.objectcode.time4u.server.web.gwt.admin.client.service;

public enum UserAccountSorting {
	USERID_ASCENDING("id", true),
	USERID_DESCENDING("id", false),
	SURNAME_ASCENDING("person.surname", true),
	SURNAME_DESCENDING("person.surname", false),
	EMAIL_ASCENDING("person.email", true),
	EMAIL_DESCENDING("person.email", false),
	LASTLOGIN_ASCENDING("lastLogin", true),
	LASTLOGIN_DESCENDING("lastLogin", false);

	private final String column;
	private final boolean ascending;

	private UserAccountSorting(String column, boolean ascending) {
		this.column = column;
		this.ascending = ascending;
	}

	public String getColumn() {
		return column;
	}

	public boolean isAscending() {
		return ascending;
	}
	
	public String getQueryString() {
		return column  + (ascending ? " asc" : " desc");
	}
}
