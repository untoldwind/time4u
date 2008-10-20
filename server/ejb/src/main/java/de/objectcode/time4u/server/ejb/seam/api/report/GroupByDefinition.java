package de.objectcode.time4u.server.ejb.seam.api.report;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "group-by")
@XmlRootElement(name = "group-by")
public class GroupByDefinition implements Serializable
{
  private static final long serialVersionUID = -2139909328575789214L;

  IProjection m_valueProjection;
  IProjection m_labelProjection;

  public GroupByDefinition()
  {
  }

  public GroupByDefinition(final IProjection valueProjection, final IProjection labelProjection)
  {
    m_valueProjection = valueProjection;
    m_labelProjection = labelProjection;
  }

  @XmlElements( { @XmlElement(name = "project", type = ProjectProjection.class),
      @XmlElement(name = "task", type = TaskProjection.class),
      @XmlElement(name = "workitem", type = WorkItemProjection.class),
      @XmlElement(name = "person", type = PersonProjection.class),
      @XmlElement(name = "dayinfo", type = DayInfoProjection.class) })
  public IProjection getValueProjection()
  {
    return m_valueProjection;
  }

  public void setValueProjection(final IProjection valueProjection)
  {
    m_valueProjection = valueProjection;
  }

  @XmlElements( { @XmlElement(name = "project", type = ProjectProjection.class),
      @XmlElement(name = "task", type = TaskProjection.class),
      @XmlElement(name = "workitem", type = WorkItemProjection.class),
      @XmlElement(name = "person", type = PersonProjection.class),
      @XmlElement(name = "dayinfo", type = DayInfoProjection.class) })
  public IProjection getLabelProjection()
  {
    return m_labelProjection;
  }

  public void setLabelProjection(final IProjection labelProjection)
  {
    m_labelProjection = labelProjection;
  }

  public ValueLabelPair project(final IRowDataAdapter rowData)
  {
    return new ValueLabelPair(m_valueProjection.project(rowData), m_labelProjection.project(rowData));
  }
}
