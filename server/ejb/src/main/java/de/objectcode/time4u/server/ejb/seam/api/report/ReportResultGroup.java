package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.List;

public class ReportResultGroup extends ReportResultBase
{
  Object m_value;
  Object m_label;

  public ReportResultGroup(final ValueLabelPair valueLabel, final List<ColumnDefinition> columns,
      final List<ColumnDefinition> groupByColumns)
  {
    super(columns, groupByColumns);

    m_value = valueLabel.getValue();
    m_label = valueLabel.getLabel();
  }

  public Object getValue()
  {
    return m_value;
  }

  public Object getLabel()
  {
    return m_label;
  }
}
