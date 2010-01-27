package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportServiceAsync {
	void generateCrossTable(CrossTableColumnType columnType,
			CrossTableRowType rowType, String projectId, Date from, Date until,
			AsyncCallback<CrossTableData> callback);

	void generatePersonWorkItemReport(String personId, List<String> projectPath,
			Date from, Date until, AsyncCallback<ReportTableData> callback);
}
