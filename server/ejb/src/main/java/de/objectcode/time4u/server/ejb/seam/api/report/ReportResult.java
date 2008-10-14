package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.LinkedList;
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

  public void addRow(final LinkedList<ValueLabelPair> groups, final Object[] row)
  {
    if (groups.isEmpty()) {
      m_rows.add(new ReportRow(m_rows.size(), row));
    } else {
      final ValueLabelPair top = groups.removeFirst();
      ReportResultGroup group = m_groups.get(top.getValue());

      if (group == null) {
        final List<ColumnDefinition> groupByColumns = new ArrayList<ColumnDefinition>();

        if (m_groupByColumns.size() > 1) {
          groupByColumns.addAll(m_groupByColumns.subList(1, m_groupByColumns.size() - 1));
        }
        group = new ReportResultGroup(top, m_columns, groupByColumns);

        m_groups.put(group.getValue(), group);
      }

      group.addRow(groups, row);
    }
  }
}
