package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReportResultBase
{
  protected List<ColumnDefinition> m_columns;
  protected List<ColumnDefinition> m_groupByColumns;
  protected List<ReportRow> m_rows;
  protected Map<Object, ReportResultGroup> m_groups;

  protected ReportResultBase(final List<ColumnDefinition> columns, final List<ColumnDefinition> groupByColumns)
  {
    m_columns = columns;
    m_groupByColumns = groupByColumns;
    m_rows = new ArrayList<ReportRow>();
    m_groups = new TreeMap<Object, ReportResultGroup>();
  }

  public List<ColumnDefinition> getColumns()
  {
    return m_columns;
  }

  public List<ColumnDefinition> getGroupByColumns()
  {
    return m_groupByColumns;
  }

  public List<ReportRow> getRows()
  {
    return m_rows;
  }

  public boolean isHasGroups()
  {
    return !m_groupByColumns.isEmpty();
  }

  public List<ReportResultGroup> getGroups()
  {
    return new ArrayList<ReportResultGroup>(m_groups.values());
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
          groupByColumns.addAll(m_groupByColumns.subList(1, m_groupByColumns.size() ));
        }
        group = new ReportResultGroup(top, m_columns, groupByColumns);

        m_groups.put(group.getValue(), group);
      }

      group.addRow(groups, row);
    }
  }
}
