package de.objectcode.time4u.server.ejb.seam.api.report;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "parameter")
@XmlRootElement(name = "parameter")
public class ReportParameterDefinition implements Serializable
{
  private static final long serialVersionUID = -5009598721487830111L;

  private String m_name;
  private ReportParameterType m_type;

  @XmlAttribute
  @XmlID
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @XmlAttribute
  public ReportParameterType getType()
  {
    return m_type;
  }

  public void setType(final ReportParameterType type)
  {
    m_type = type;
  }

}
