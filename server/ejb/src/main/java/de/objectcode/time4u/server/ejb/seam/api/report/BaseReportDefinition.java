package de.objectcode.time4u.server.ejb.seam.api.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.filter.AndFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.OrFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.ParameterRef;
import de.objectcode.time4u.server.ejb.seam.api.filter.PersonFilter;

@XmlType(name = "report")
public abstract class BaseReportDefinition implements Serializable
{
  private static final long serialVersionUID = -8401403739035061630L;

  protected String m_name;
  protected String m_description;
  protected IFilter m_filter;
  protected List<ReportParameterDefinition> m_parameters;

  protected BaseReportDefinition()
  {
    m_parameters = new ArrayList<ReportParameterDefinition>();
  }

  @XmlAttribute
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @XmlElement(name = "description", namespace = "http://objectcode.de/time4u/ejb/seam/report")
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @XmlElementRefs( { @XmlElementRef(type = AndFilter.class), @XmlElementRef(type = OrFilter.class),
      @XmlElementRef(type = DateRangeFilter.class), @XmlElementRef(type = ParameterRef.class),
      @XmlElementRef(type = PersonFilter.class) })
  public IFilter getFilter()
  {
    return m_filter;
  }

  public void setFilter(final IFilter filter)
  {
    m_filter = filter;
  }

  @XmlElementWrapper(name = "parameters", namespace = "http://objectcode.de/time4u/ejb/seam/report")
  @XmlElementRef
  public List<ReportParameterDefinition> getParameters()
  {
    return m_parameters;
  }

  public void setParameters(final List<ReportParameterDefinition> parameters)
  {
    m_parameters = parameters;
  }

  public void addParameter(final ReportParameterDefinition parameter)
  {
    m_parameters.add(parameter);
  }

  @XmlTransient
  public abstract EntityType getEntityType();

  public abstract ReportResult createResult();

  public abstract void collect(final IRowDataAdapter rowData, final ReportResult reportResult);
}
