package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.List;

public class ReportResult
{
  String m_name;
  List<ColumnDefinition> m_columns;

  public ReportResult(final String name, final List<ColumnDefinition> columns)
  {
    m_name = name;
    m_columns = columns;
  }

}
