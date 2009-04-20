package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.Map;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

@XmlType(name = "parameter-ref")
@XmlRootElement(name = "parameter-ref")
public class ParameterRef implements IFilter
{
  private static final long serialVersionUID = -1223023814968850580L;

  ReportParameterDefinition m_parameter;

  public ParameterRef()
  {
  }

  public ParameterRef(final ReportParameterDefinition parameter)
  {
    m_parameter = parameter;
  }

  @XmlAttribute
  @XmlIDREF
  public ReportParameterDefinition getParameter()
  {
    return m_parameter;
  }

  public void setParameter(final ReportParameterDefinition parameter)
  {
    m_parameter = parameter;
  }

  /**
   * {@inheritDoc}
   */
  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters)
  {
    final BaseParameterValue parameterValue = parameters.get(m_parameter.getName());

    return parameterValue.getFilter().getWhereClause(entityType, parameters);
  }

  /**
   * {@inheritDoc}
   */
  public void setQueryParameters(final EntityType entityType, final Query query,
      final Map<String, BaseParameterValue> parameters)
  {
    final BaseParameterValue parameterValue = parameters.get(m_parameter.getName());

    parameterValue.getFilter().setQueryParameters(entityType, query, parameters);
  }

}
