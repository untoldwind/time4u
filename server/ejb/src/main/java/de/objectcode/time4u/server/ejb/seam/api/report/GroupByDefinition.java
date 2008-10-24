package de.objectcode.time4u.server.ejb.seam.api.report;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "group-by")
@XmlRootElement(name = "group-by")
public class GroupByDefinition implements Serializable
{
  private static final long serialVersionUID = -2139909328575789214L;

  public enum Mode
  {
    LIST(false),
    LIST_WITH_AGGREGATE(true);

    private boolean m_aggregate;

    private Mode(final boolean aggregate)
    {
      m_aggregate = aggregate;
    }

    public boolean isAggregate()
    {
      return m_aggregate;
    }
  }

  private IProjection m_valueProjection;
  private IProjection m_labelProjection;
  private Mode m_mode;

  public GroupByDefinition()
  {
    m_mode = Mode.LIST;
  }

  public GroupByDefinition(final IProjection valueProjection, final IProjection labelProjection)
  {
    m_valueProjection = valueProjection;
    m_labelProjection = labelProjection;
    m_mode = Mode.LIST;
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

  @XmlAttribute
  public Mode getMode()
  {
    return m_mode;
  }

  public void setMode(final Mode mode)
  {
    m_mode = mode;
  }
}
