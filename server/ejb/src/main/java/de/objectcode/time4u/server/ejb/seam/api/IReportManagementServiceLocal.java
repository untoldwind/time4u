package de.objectcode.time4u.server.ejb.seam.api;

import de.objectcode.time4u.server.entities.report.ReportDefinitionEntity;

public interface IReportManagementServiceLocal
{
  void initReportDefinitions();

  ReportDefinitionEntity getReportDefinitionEntity(final String reportId);

  void storeReportDefinitionEntity(final ReportDefinitionEntity reportDefinitionEntity);

  void deleteReportDeinfitionEntity(final String reportId);
}
