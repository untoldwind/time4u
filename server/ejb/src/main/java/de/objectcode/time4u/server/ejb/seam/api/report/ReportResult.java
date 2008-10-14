package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.List;

public class ReportResult extends ReportResultBase
{
  String m_name;

  public ReportResult(final String name, final List<ColumnDefinition> columns,
      final List<ColumnDefinition> groupByColumns)
  {
    super(columns, groupByColumns);

    m_name = name;
  }

  public String getName()
  {
    return m_name;
  }

}
