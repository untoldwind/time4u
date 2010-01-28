package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("report.service")
public interface ReportService extends RemoteService {
	ReportTableData generatePersonWorkItemReport(String personId, List<String> projectPath, Date from, Date until);
	
	CrossTableData generateCrossTable(CrossTableColumnType columnType, CrossTableRowType rowType, String projectId, Date from, Date until);
}
