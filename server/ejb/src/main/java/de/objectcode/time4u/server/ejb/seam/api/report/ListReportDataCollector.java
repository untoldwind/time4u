package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ListReportDataCollector implements IReportDataCollector
{
  List<IProjection> m_projections;
  List<GroupByDefinition> m_groupByDefinitions;
  ReportResult m_reportResult;

  ListReportDataCollector(final String name, final List<IProjection> projections,
      final List<GroupByDefinition> groupByDefinitions)
  {
    m_projections = Collections.unmodifiableList(projections);
    m_groupByDefinitions = Collections.unmodifiableList(groupByDefinitions);

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

    m_reportResult = new ReportResult(name, columns, groupByColumns);
  }

  public ReportResult getReportResult()
  {
    return m_reportResult;
  }

  public void collect(final IRowDataAdapter rowData)
  {
    final Object[] row = new Object[m_projections.size()];

    for (int i = 0; i < row.length; i++) {
      row[i] = m_projections.get(i).project(rowData);
    }

    final LinkedList<ValueLabelPair> groups = new LinkedList<ValueLabelPair>();

    for (final GroupByDefinition groupBy : m_groupByDefinitions) {
      groups.add(groupBy.project(rowData));
    }

    m_reportResult.addRow(groups, row);
  }

}
