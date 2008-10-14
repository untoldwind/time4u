package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReportResultGroup
{
  Object m_value;
  Object m_label;
  List<ReportRow> m_rows;
  Map<Object, ReportResultGroup> m_groups;

  public ReportResultGroup(final ValueLabelPair valueLabel)
  {
    m_value = valueLabel.getValue();
    m_label = valueLabel.getLabel();
    m_rows = new ArrayList<ReportRow>();
    m_groups = new TreeMap<Object, ReportResultGroup>();
  }

  public Object getValue()
  {
    return m_value;
  }

  public Object getLabel()
  {
    return m_label;
  }

  public List<ReportRow> getRows()
  {
    return m_rows;
  }

  public boolean isHasGroups()
  {
    return !m_groups.isEmpty();
  }
  public Collection<ReportResultGroup> getGroups()
  {
    return m_groups.values();
  }

  public void addRow(final LinkedList<ValueLabelPair> groups, final Object[] row)
  {
    if (groups.isEmpty()) {
      m_rows.add(new ReportRow(m_rows.size(), row));
    } else {
      final ValueLabelPair top = groups.removeFirst();
      ReportResultGroup group = m_groups.get(top.getValue());

      if (group == null) {
        group = new ReportResultGroup(top);

        m_groups.put(group.getValue(), group);
      }

      group.addRow(groups, row);
    }
  }
}
