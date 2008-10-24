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

  @XmlElements( {
      @XmlElement(name = "value-project", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = ProjectProjection.class),
      @XmlElement(name = "value-task", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = TaskProjection.class),
      @XmlElement(name = "value-workitem", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = WorkItemProjection.class),
      @XmlElement(name = "value-person", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = PersonProjection.class),
      @XmlElement(name = "value-dayinfo", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = DayInfoProjection.class) })
  public IProjection getValueProjection()
  {
    return m_valueProjection;
  }

  public void setValueProjection(final IProjection valueProjection)
  {
    m_valueProjection = valueProjection;
  }

  @XmlElements( {
      @XmlElement(name = "label-project", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = ProjectProjection.class),
      @XmlElement(name = "label-task", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = TaskProjection.class),
      @XmlElement(name = "label-workitem", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = WorkItemProjection.class),
      @XmlElement(name = "label-person", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = PersonProjection.class),
      @XmlElement(name = "label-dayinfo", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = DayInfoProjection.class) })
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
