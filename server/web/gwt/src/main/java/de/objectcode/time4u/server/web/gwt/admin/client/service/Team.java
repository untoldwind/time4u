package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.List;

public class Team extends TeamSummary {

	List<PersonSummary> owners;
	List<PersonSummary> members;

	public Team() {
		super();
	}

	public Team(String id, String name, String description,
			List<PersonSummary> owners, List<PersonSummary> members) {
		super(id, name, description);

		this.owners = owners;
		this.members = members;
	}

	public List<PersonSummary> getOwners() {
		return owners;
	}

	public List<PersonSummary> getMembers() {
		return members;
	}

}
