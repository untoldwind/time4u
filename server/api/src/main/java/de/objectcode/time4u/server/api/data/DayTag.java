package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "day-tag")
@XmlRootElement(name = "day-tag")
public class DayTag
{
  private String m_name;
  private String m_label;
  private String m_description;
  private Integer m_regularTime;
  private boolean m_deleted;

  @XmlAttribute
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @XmlAttribute
  public String getLabel()
  {
    return m_label;
  }

  public void setLabel(final String label)
  {
    m_label = label;
  }

  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @XmlAttribute
  public Integer getRegularTime()
  {
    return m_regularTime;
  }

  public void setRegularTime(final Integer regularTime)
  {
    m_regularTime = regularTime;
  }

  @XmlAttribute
  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }
}
