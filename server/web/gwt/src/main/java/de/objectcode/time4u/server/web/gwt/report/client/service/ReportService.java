package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("report.service")
public interface ReportService extends RemoteService {
	CrossTable generateCrossTable(CrossTableColumnType columnType, CrossTableRowType rowType, String projectId, Date from, Date until);
}
