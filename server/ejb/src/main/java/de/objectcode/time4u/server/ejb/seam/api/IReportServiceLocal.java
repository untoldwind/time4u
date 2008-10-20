package de.objectcode.time4u.server.ejb.seam.api;

import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;

public interface IReportServiceLocal
{
  ReportResult generateReport(BaseReportDefinition reportDefinition);
}
