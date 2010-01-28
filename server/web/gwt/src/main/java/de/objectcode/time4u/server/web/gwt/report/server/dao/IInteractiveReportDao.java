package de.objectcode.time4u.server.web.gwt.report.server.dao;

import java.util.Date;

import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableData;

public interface IInteractiveReportDao {
	CrossTableData generateProjectPersonCrossTable(String mainProjectId, Date from,
			Date until, String personId);

	CrossTableData generateProjectTeamCrossTable(String mainProjectId, Date from,
			Date until, String personId);

	CrossTableData generateTaskPersonCrossTable(String lastProjectId, Date from,
			Date until, String personId);

	CrossTableData generateTaskTeamCrossTable(String lastProjectId, Date from,
			Date until, String personId);
}
