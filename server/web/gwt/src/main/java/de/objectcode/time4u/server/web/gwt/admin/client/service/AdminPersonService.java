package de.objectcode.time4u.server.web.gwt.admin.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("adminPerson.service")
public interface AdminPersonService  extends RemoteService {
	UserAccount.Page getUserAccounts(int pageNumber, int pageSize, UserAccount.Projections sorting, boolean ascending);

	PersonSummary.Page getPersonSummaries(int pageNumber, int pageSize, PersonSummary.Projections sorting, boolean ascending);

	Person getPerson(String personId);
	
	TeamSummary.Page getTeamSummaries(int pageNumber, int pageSize, TeamSummary.Projections sorting, boolean ascending);

	Team getTeam(String teamId);
}
