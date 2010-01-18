package de.objectcode.time4u.server.web.gwt.admin.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserAccount implements IsSerializable {
	private String userId;
	private Person person;

	public UserAccount() {
	}

	public UserAccount(String userId, Person person) {
		this.userId = userId;
		this.person = person;
	}

	public String getUserId() {
		return userId;
	}

	public Person getPerson() {
		return person;
	}

}
