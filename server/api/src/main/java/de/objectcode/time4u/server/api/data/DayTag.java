package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "day-tag")
@XmlRootElement(name = "day-tag")
public class DayTag
{
  private String m_name;
  private String m_description;
  private Integer m_regularTime;

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

}
