package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;

@XmlType(name = "dayinfo-report")
@XmlRootElement(name = "dayinfo-report")
public class DayInfoReportDefinition extends BaseReportDefinition
{
  private static final long serialVersionUID = -7711179139305455721L;

  List<IProjection> m_projections;
  List<GroupByDefinition> m_groupByDefinitions;

  public DayInfoReportDefinition()
  {
    m_projections = new ArrayList<IProjection>();
    m_groupByDefinitions = new ArrayList<GroupByDefinition>();
  }

  @XmlElementWrapper(name = "projections", namespace = "http://objectcode.de/time4u/ejb/seam/report")
  @XmlElements( {
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
    return EntityType.DAYINFO;
  }

  @Override
  public IReportDataCollector createDataCollector()
  {
    return new ListReportDataCollector(m_name, m_projections, m_groupByDefinitions);
  }

}
