package de.objectcode.time4u.server.web.gwt.report.server.dao;

import java.util.Date;

import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTable;

public interface IInteractiveReportDao {
	CrossTable generateProjectPersonCrossTable(String mainProjectId, Date from,
			Date until, String personId);

	CrossTable generateProjectTeamCrossTable(String mainProjectId, Date from,
			Date until, String personId);

	CrossTable generateTaskPersonCrossTable(String lastProjectId, Date from,
			Date until, String personId);

	CrossTable generateTaskTeamCrossTable(String lastProjectId, Date from,
			Date until, String personId);
}
