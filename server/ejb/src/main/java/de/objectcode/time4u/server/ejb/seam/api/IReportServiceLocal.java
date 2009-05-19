package de.objectcode.time4u.server.ejb.seam.api;

import java.util.Date;
import java.util.Map;

import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.CrossTableResult;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

/**
 * Local report service interface.
 * 
 * The report service is an abstract reporting engine to generate reports based on workitems, dayinfos and todos.
 * 
 * @author junglas
 */
public interface IReportServiceLocal
{
  /**
   * Generate a report.
   * 
   * @param reportDefinition
   *          The definition of the report
   * @param parameters
   *          The parameter values (depending on parameters definied in the report definition)
   * @return The result of the report
   */
  ReportResult generateReport(BaseReportDefinition reportDefinition, Map<String, BaseParameterValue> parameters);

  /**
   * Temporary helper until we have found a way to abstractify this.
   */
  CrossTableResult generateProjectPersonCrossTable(String mainProjectId, Date from, Date until);
}
