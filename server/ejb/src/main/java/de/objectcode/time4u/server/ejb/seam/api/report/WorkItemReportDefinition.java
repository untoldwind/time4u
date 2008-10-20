package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;

@XmlType(name = "workitem-report")
@XmlRootElement(name = "workitem-report")
public class WorkItemReportDefinition extends BaseReportDefinition
{
  private static final long serialVersionUID = 4683950609367326486L;

  List<IProjection> m_projections;
  List<GroupByDefinition> m_groupByDefinitions;

  public WorkItemReportDefinition()
  {
    m_projections = new ArrayList<IProjection>();
    m_groupByDefinitions = new ArrayList<GroupByDefinition>();
  }

  @XmlElementWrapper(name = "projections", namespace = "http://objectcode.de/time4u/ejb/seam/report")
  @XmlElements( {
      @XmlElement(name = "project", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = ProjectProjection.class),
      @XmlElement(name = "task", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = TaskProjection.class),
      @XmlElement(name = "workitem", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = WorkItemProjection.class),
      @XmlElement(name = "person", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = PersonProjection.class),
      @XmlElement(name = "dayinfo", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = DayInfoProjection.class) })
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

  @XmlElementWrapper(name = "group-bys", namespace = "http://objectcode.de/time4u/ejb/seam/report")
  @XmlElementRef
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
