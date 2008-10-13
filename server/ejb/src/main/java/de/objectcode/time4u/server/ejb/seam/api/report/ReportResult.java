package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

public class ReportResult
{
  String m_name;
  List<ColumnDefinition> m_columns;
  List<ReportRow> m_rows;

  public ReportResult(final String name, final List<ColumnDefinition> columns)
  {
    m_name = name;
    m_columns = columns;
    m_rows = new ArrayList<ReportRow>();
  }

  public String getName()
  {
    return m_name;
  }

  public List<ColumnDefinition> getColumns()
  {
    return m_columns;
  }

  public List<ReportRow> getRows()
  {
    return m_rows;
  }

  public void addRow(final Object[] row)
  {
    m_rows.add(new ReportRow(m_rows.size(), row));
  }
}
