package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.objectcode.time4u.server.web.gwt.utils.client.service.IProjection;

public class UserAccount implements IsSerializable {

	public static enum Projections implements IProjection<UserAccount> {
		ACTIVE("person.active", false) {
			public Object project(UserAccount dto) {
				return dto.getPerson().isActive();
			}
		},
		USERID("id", true) {
			public Object project(UserAccount dto) {
				return dto.getUserId();
			}
		},
		SURNAME("person.surname", true) {
			public Object project(UserAccount dto) {
				return (dto.getPerson().getGivenName() != null
						&& dto.getPerson().getGivenName().length() > 0 ? (dto
						.getPerson().getGivenName() + " ") : "")
						+ dto.getPerson().getSurname();
			}
		},
		EMAIL("person.email", true) {
			public Object project(UserAccount dto) {
				return dto.getPerson().getEmail();
			}
		},
		LASTLOGIN("lastLogin", true) {
			public Object project(UserAccount dto) {
				return dto.getLastLogin();
			}
		};

		private final String column;
		private final boolean sortable;

		private Projections(String column, boolean sortable) {
			this.column = column;
			this.sortable = sortable;
		}

		public String getColumn() {
			return column;
		}

		public boolean isSortable() {
			return sortable;
		}
	}
	
	private String userId;
	private Person person;
	private Date lastLogin;

	public UserAccount() {
	}

	public UserAccount(String userId, Person person, Date lastLogin) {
		this.userId = userId;
		this.person = person;
		this.lastLogin = lastLogin;
	}

	public String getUserId() {
		return userId;
	}

	public Person getPerson() {
		return person;
	}

	public Date getLastLogin() {
		return lastLogin;
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
