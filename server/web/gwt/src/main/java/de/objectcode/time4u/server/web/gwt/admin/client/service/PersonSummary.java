package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.objectcode.time4u.server.web.gwt.utils.client.service.BaseDataPage;
import de.objectcode.time4u.server.web.gwt.utils.client.service.IProjection;

public class PersonSummary implements IsSerializable {

	public static enum Projections implements IProjection<PersonSummary> {
		ACTIVE("person.active", false) {
			public Object project(PersonSummary dto) {
				return dto.isActive();
			}
		},
		SURNAME("surname", true) {
			public Object project(PersonSummary dto) {
				return dto.getSurname();
			}
		},
		GIVENNAME("givenName", true) {
			public Object project(PersonSummary dto) {
				return dto.getGivenName();
			}
		},
		EMAIL("email", true) {
			public Object project(PersonSummary dto) {
				return dto.getEmail();
			}
		},
		LASTSYNCHRONIZE("lastSynchronize", true) {
			public Object project(PersonSummary dto) {
				return dto.getLastSynchronized();
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

	public static class Page extends BaseDataPage<PersonSummary> {

		public Page() {

		}

		public Page(int pageNumber, int pageSize, int totalNumber,
				List<PersonSummary> pageData) {
			super(pageNumber, pageSize, totalNumber);

			this.pageData = pageData;
		}

		private List<PersonSummary> pageData;

		public List<PersonSummary> getPageData() {
			return pageData;
		}
	}

	private String id;
	private boolean active;
	private String givenName;
	private String surname;
	private String email;
	private Date lastSynchronized;

	public PersonSummary() {
	}

	public PersonSummary(String id, boolean active, String givenName,
			String surname, String email, Date lastSynchronized) {
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

	@Override
	public int hashCode() {
		return ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonSummary other = (PersonSummary) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
