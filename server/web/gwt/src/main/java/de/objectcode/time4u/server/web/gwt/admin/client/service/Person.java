package de.objectcode.time4u.server.web.gwt.admin.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Person implements IsSerializable {
	private String id;
	private String givenName;
	private String surname;
	private String email;

	public Person() {
	}

	public Person(String id, String givenName, String surname, String email) {
		this.id = id;
		this.givenName = givenName;
		this.surname = surname;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

}
