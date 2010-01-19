package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Person implements IsSerializable {
	private String id;
	private boolean active;
	private String givenName;
	private String surname;
	private String email;
	private Date lastSynchronized;

	public Person() {
	}

	public Person(String id, boolean active, String givenName, String surname,
			String email, Date lastSynchronized) {
		this.id = id;
		this.active = active;
		this.givenName = givenName;
		this.surname = surname;
		this.email = email;
		this.lastSynchronized = lastSynchronized;
	}

	public String getId() {
		return id;
	}

	public boolean isActive() {
		return active;
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

	public Date getLastSynchronized() {
		return lastSynchronized;
	}

}
