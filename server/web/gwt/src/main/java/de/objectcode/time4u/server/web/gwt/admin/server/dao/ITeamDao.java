package de.objectcode.time4u.server.web.gwt.admin.server.dao;

import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;

public interface ITeamDao {
	TeamSummary.Page findTeamSummaryPage(int pageNumber, int pageSize,  TeamSummary.Projections sorting, boolean ascending);	

}
