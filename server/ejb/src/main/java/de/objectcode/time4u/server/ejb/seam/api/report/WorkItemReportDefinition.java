package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

public class WorkItemReportDefinition extends BaseReportDefinition
{
  List<IProjection> m_projections;
  List<GroupByDefinition> m_groupByDefinitions;

  public List<IProjection> getProjections()
  {
    return m_projections;
  }

  public void setProjections(final List<IProjection> projections)
  {
    m_projections = projections;
  }

  public void addProjection(final IProjection projection)
  {
    if (m_projections == null) {
      m_projections = new ArrayList<IProjection>();
    }
    m_projections.add(projection);
  }

  public List<GroupByDefinition> getGroupByDefinitions()
  {
    return m_groupByDefinitions;
  }

  public void setGroupByDefinitions(final List<GroupByDefinition> groupByDefinitions)
  {
    m_groupByDefinitions = groupByDefinitions;
  }

  public ReportResult createResult()
  {
    final List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

    int index = 0;
    for (final IProjection projection : m_projections) {
      columns.add(projection.getColumnDefinition(index++));
    }

    return new ReportResult(m_name, columns);
  }

  public void collect(final IRowDataAdapter rowData, final ReportResult reportResult)
  {
    final Object[] row = new Object[m_projections.size()];

    for (int i = 0; i < row.length; i++) {
      final Object[] data = m_projections.get(i).project(rowData);

      if (data.length == 1) {
        row[i] = data[0];
      } else if (data.length > 1) {
        final StringBuffer buffer = new StringBuffer();

        for (int j = 0; j < data.length; j++) {
          if (j > 0) {
            buffer.append(", ");
          }
          buffer.append(data[j]);
        }
        row[i] = buffer.toString();
      }
    }

    reportResult.addRow(row);
  }
}
