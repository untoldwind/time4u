package de.objectcode.time4u.server.ejb.seam.api;

import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.WorkItemReportDefinition;

public interface IReportServiceLocal
{
  ReportResult workItemReport(WorkItemReportDefinition reportDefinition);
}
