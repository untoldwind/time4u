package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListReportDataCollector implements IReportDataCollector
{
  private final List<IProjection> m_projections;
  private final List<GroupByDefinition> m_groupByDefinitions;
  private final ReportResult m_reportResult;
  private final Map<List<Object>, IAggregation[]> m_aggregations;
  private final boolean m_aggregate;

  ListReportDataCollector(final String name, final List<IProjection> projections, final boolean aggregate,
      final List<GroupByDefinition> groupByDefinitions)
  {
    m_aggregations = new HashMap<List<Object>, IAggregation[]>();
    m_projections = Collections.unmodifiableList(projections);
    m_groupByDefinitions = Collections.unmodifiableList(groupByDefinitions);
    m_aggregate = aggregate;

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

    if (m_aggregate) {
      aggregate(null, row);
    }

    final LinkedList<ValueLabelPair> groups = new LinkedList<ValueLabelPair>();
    final List<Object> groupKey = new ArrayList<Object>();

    for (final GroupByDefinition groupBy : m_groupByDefinitions) {
      final ValueLabelPair group = groupBy.project(rowData);
      groups.add(group);
      groupKey.add(group.getValue());

      if (groupBy.getMode().isAggregate()) {
        aggregate(groupKey, row);
      }
    }

    m_reportResult.addRow(groups, row);
  }

  public void finish()
  {
    for (final Map.Entry<List<Object>, IAggregation[]> entry : m_aggregations.entrySet()) {
      final IAggregation[] aggregations = entry.getValue();
      final Object[] aggregates = new Object[aggregations.length];

      for (int i = 0; i < aggregations.length; i++) {
        if (aggregations[i] != null) {
          aggregations[i].finish();
          aggregates[i] = aggregations[i].getAggregate();
        }
      }

      m_reportResult.setAggregates(entry.getKey(), aggregates);
    }
  }

  private void aggregate(final List<Object> key, final Object[] row)
  {
    IAggregation[] aggregations = m_aggregations.get(key);

    if (aggregations == null) {
      aggregations = new IAggregation[m_projections.size()];

      for (int i = 0; i < aggregations.length; i++) {
        aggregations[i] = m_projections.get(i).createAggregation();
      }

      m_aggregations.put(key, aggregations);
    }

    for (int i = 0; i < aggregations.length; i++) {
      if (aggregations[i] != null) {
        aggregations[i].collect(row[i]);
      }
    }
  }
}
