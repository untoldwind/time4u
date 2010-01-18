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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		UserAccount other = (UserAccount) obj;

		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return (userId == null) ? 0 : userId.hashCode();
	}

}
