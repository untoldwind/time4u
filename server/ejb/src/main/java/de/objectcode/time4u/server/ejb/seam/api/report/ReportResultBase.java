package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Base class of a report result.
 * 
 * Note that report may contain hierarchic group-by results. I.e. the ReportResult may not contain any rows but a map of
 * ReportResultGroup instead. Each of these ReportResultGroup may contain rows or a map of sub-groups.
 * 
 * @author junglas
 */
public class ReportResultBase
{
  protected List<ColumnDefinition> m_columns;
  protected List<ColumnDefinition> m_groupByColumns;
  protected List<ReportRow> m_rows;
  protected Object[] m_aggregates;
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

  public boolean isHasAggregates()
  {
    return m_aggregates != null;
  }

  public Object[] getAggregates()
  {
    return m_aggregates;
  }

  public void setAggregates(final List<Object> groupKey, final Object[] aggregates)
  {
    if (groupKey == null) {
      m_aggregates = aggregates;
    } else {
      ReportResultBase current = this;

      for (final Object key : groupKey) {
        if (current == null) {
          break;
        }
        current = current.m_groups.get(key);
      }

      if (current != null) {
        current.m_aggregates = aggregates;
      }
    }
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
          groupByColumns.addAll(m_groupByColumns.subList(1, m_groupByColumns.size()));
        }
        group = new ReportResultGroup(top, m_columns, groupByColumns);

        m_groups.put(group.getValue(), group);
      }

      group.addRow(groups, row);
    }
  }
}
