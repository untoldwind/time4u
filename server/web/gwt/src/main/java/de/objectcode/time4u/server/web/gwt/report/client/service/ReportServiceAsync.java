package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportServiceAsync {

	void generateProjectPersonCrossTable(String mainProjectId, Date from,
			Date until, AsyncCallback<CrossTable> callback);

	void generateProjectTeamCrossTable(String mainProjectId, Date from,
			Date until, AsyncCallback<CrossTable> callback);

	void generateTaskPersonCrossTable(String lastProjectId, Date from,
			Date until, AsyncCallback<CrossTable> callback);

	void generateTaskTeamCrossTable(String lastProjectId, Date from,
			Date until, AsyncCallback<CrossTable> callback);

}
