package de.objectcode.time4u.server.ejb.seam.api;

import java.util.Map;

import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

public interface IReportServiceLocal
{
  ReportResult generateReport(BaseReportDefinition reportDefinition, Map<String, BaseParameterValue> parameters);
}
