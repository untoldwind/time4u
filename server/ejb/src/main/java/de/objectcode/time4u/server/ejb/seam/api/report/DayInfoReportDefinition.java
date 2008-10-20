package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;

@XmlType(name = "dayinfo-report")
@XmlRootElement(name = "dayinfo-report")
public class DayInfoReportDefinition extends BaseReportDefinition
{
  List<IProjection> m_projections;
  List<GroupByDefinition> m_groupByDefinitions;

  public DayInfoReportDefinition()
  {
    m_projections = new ArrayList<IProjection>();
    m_groupByDefinitions = new ArrayList<GroupByDefinition>();
  }

  @XmlElementWrapper
  @XmlElements( { @XmlElement(name = "project", type = ProjectProjection.class),
      @XmlElement(name = "task", type = TaskProjection.class),
      @XmlElement(name = "workitem", type = WorkItemProjection.class),
      @XmlElement(name = "person", type = PersonProjection.class),
      @XmlElement(name = "dayinfo", type = DayInfoProjection.class) })
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
    return EntityType.DAYINFO;
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
