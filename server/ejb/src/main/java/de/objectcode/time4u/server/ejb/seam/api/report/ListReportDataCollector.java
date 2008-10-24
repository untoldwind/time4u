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
  private final Map<Object[], IAggregation[]> m_aggregations;

  ListReportDataCollector(final String name, final List<IProjection> projections, final boolean aggregate,
      final List<GroupByDefinition> groupByDefinitions)
  {
    m_aggregations = new HashMap<Object[], IAggregation[]>();
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

    if (aggregate) {
      final IAggregation[] aggregations = new IAggregation[m_projections.size()];

      for (int i = 0; i < aggregations.length; i++) {
        aggregations[i] = m_projections.get(i).createAggregation();
      }

      m_aggregations.put(null, aggregations);
    }
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

    aggregate(null, row);
    final LinkedList<ValueLabelPair> groups = new LinkedList<ValueLabelPair>();

    for (final GroupByDefinition groupBy : m_groupByDefinitions) {
      groups.add(groupBy.project(rowData));
    }

    m_reportResult.addRow(groups, row);
  }

  public void finish()
  {
    for (final IAggregation[] aggregations : m_aggregations.values()) {
      for (int i = 0; i < aggregations.length; i++) {
        if (aggregations[i] != null) {
          aggregations[i].finish();
        }
      }
    }

    final IAggregation[] mainAggregations = m_aggregations.get(null);
    if (mainAggregations != null) {
      final Object[] aggregates = new Object[mainAggregations.length];

      for (int i = 0; i < mainAggregations.length; i++) {
        if (mainAggregations[i] != null) {
          aggregates[i] = mainAggregations[i].getAggregate();
        }
      }

      m_reportResult.setAggregates(aggregates);
    }
  }

  private void aggregate(final Object[] key, final Object[] row)
  {
    final IAggregation[] aggregations = m_aggregations.get(key);

    if (aggregations != null) {
      for (int i = 0; i < aggregations.length; i++) {
        if (aggregations[i] != null) {
          aggregations[i].collect(row[i]);
        }
      }
    }

  }
}
