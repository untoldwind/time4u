package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.Date;
import java.util.List;

public class Person extends PersonSummary {
	List<TeamSummary> ownerOf;
	List<TeamSummary> memberOf;

	public Person() {
		super();
	}

	public Person(String id, boolean active, String givenName, String surname,
			String email, Date lastSynchronized, List<TeamSummary> ownerOf,
			List<TeamSummary> memberOf) {
		super(id, active, givenName, surname, email, lastSynchronized);

		this.ownerOf = ownerOf;
		this.memberOf = memberOf;
	}

	public List<TeamSummary> getOwnerOf() {
		return ownerOf;
	}

	public List<TeamSummary> getMemberOf() {
		return memberOf;
	}

}
