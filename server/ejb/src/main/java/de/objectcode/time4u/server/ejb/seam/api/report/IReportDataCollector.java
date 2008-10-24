package de.objectcode.time4u.server.ejb.seam.api.report;

public interface IReportDataCollector
{
  ReportResult getReportResult();

  void collect(final IRowDataAdapter rowData);

  void finish();
}
