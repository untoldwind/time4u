package de.objectcode.time4u.server.web.gwt.admin.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminPersonServiceAsync {

	void getUserAccounts(int pageNumber, int pageSize,UserAccount.Projections sorting, boolean ascending,
			AsyncCallback<UserAccount.Page> callback);

	void getPersonSummaries(int pageNumber, int pageSize, PersonSummary.Projections sorting,
			boolean ascending, AsyncCallback<PersonSummary.Page> callback);

	void getTeamSummaries(int pageNumber, int pageSize, TeamSummary.Projections sorting,
			boolean ascending, AsyncCallback<TeamSummary.Page> callback);

	void getPerson(String personId, AsyncCallback<Person> callback);

	void getTeam(String teamId, AsyncCallback<Team> callback);
}
