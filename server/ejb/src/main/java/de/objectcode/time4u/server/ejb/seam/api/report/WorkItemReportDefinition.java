package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.objectcode.time4u.server.api.data.EntityType;

public class WorkItemReportDefinition extends BaseReportDefinition
{
  List<IProjection> m_projections;
  List<GroupByDefinition> m_groupByDefinitions;

  public WorkItemReportDefinition()
  {
    m_projections = new ArrayList<IProjection>();
    m_groupByDefinitions = new ArrayList<GroupByDefinition>();
  }

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

  public void addGroupByDefinition(final GroupByDefinition groupBy)
  {
    m_groupByDefinitions.add(groupBy);
  }

  @Override
  public EntityType getEntityType()
  {
    return EntityType.WORKITEM;
  }

  @Override
  public ReportResult createResult()
  {
    final List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

    int index = 0;
    for (final IProjection projection : m_projections) {
      columns.add(projection.getColumnDefinition(index++));
    }

    final List<ColumnDefinition> groupByColumns = new ArrayList<ColumnDefinition>();
    index = 0;
    for (final GroupByDefinition groupBy : m_groupByDefinitions) {
      groupByColumns.add(groupBy.getLabelProjection().getColumnDefinition(index++));
    }

    return new ReportResult(m_name, columns, groupByColumns);
  }

  @Override
  public void collect(final IRowDataAdapter rowData, final ReportResult reportResult)
  {
    final Object[] row = new Object[m_projections.size()];

    for (int i = 0; i < row.length; i++) {
      row[i] = m_projections.get(i).project(rowData);
    }

    final LinkedList<ValueLabelPair> groups = new LinkedList<ValueLabelPair>();

    for (final GroupByDefinition groupBy : m_groupByDefinitions) {
      groups.add(groupBy.project(rowData));
    }

    reportResult.addRow(groups, row);
  }
}
