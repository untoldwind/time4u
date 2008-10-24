package de.objectcode.time4u.server.ejb.seam.api.report;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

/**
 * Definition of a report parameter.
 * 
 * Report parameters are queried from the user before the report can be created.
 * 
 * @author junglas
 */
@XmlType(name = "parameter")
@XmlRootElement(name = "parameter")
public class ReportParameterDefinition implements Serializable
{
  private static final long serialVersionUID = -5009598721487830111L;

  /** Name of the parameter. */
  private String m_name;
  /** Label of the parameter to be presented in the UI. */
  private String m_label;
  /** Value type of the parameter. */
  private ReportParameterType m_type;

  public ReportParameterDefinition()
  {
  }

  public ReportParameterDefinition(final String name, final ReportParameterType type)
  {
    m_name = name;
    m_type = type;
  }

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
  public String getLabel()
  {
    return m_label;
  }

  public void setLabel(final String label)
  {
    m_label = label;
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

  public BaseParameterValue newValueInstance()
  {
    return m_type.newValueInstance(m_name, m_label);
  }
}
