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

    for (final IProjection projection : m_projections) {
      columns.add(projection.getColumnDefinition());
    }

    return new ReportResult(m_name, columns);
  }

  public void collect(final IRowDataAdapter rowData, final ReportResult reportResult)
  {

  }
}
