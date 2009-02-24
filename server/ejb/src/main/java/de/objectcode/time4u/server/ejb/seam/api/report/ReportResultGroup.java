package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.List;

/**
 * A sub-report result based on a group-by.
 * 
 * Note that a ReportResultGroup may not contain any rows but a map of sub-ReportResultGroup instead.
 * 
 * @author junglas
 */
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
