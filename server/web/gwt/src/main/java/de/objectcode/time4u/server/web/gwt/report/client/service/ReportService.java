package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("report.service")
public interface ReportService extends RemoteService {
	CrossTable generateProjectPersonCrossTable(String mainProjectId, Date from,
			Date until);

	CrossTable generateProjectTeamCrossTable(String mainProjectId, Date from,
			Date until);

	CrossTable generateTaskTeamCrossTable(String lastProjectId, Date from,
			Date until);

	CrossTable generateTaskPersonCrossTable(String lastProjectId, Date from,
			Date until);
}
