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
import de.objectcode.time4u.server.ejb.seam.api.filter.ProjectFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.ProjectPathFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.TaskFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.TodoStateFilter;

/**
 * A generic report definition.
 * 
 * @author junglas
 */
@XmlType(name = "report")
public abstract class BaseReportDefinition implements Serializable
{
  private static final long serialVersionUID = -8401403739035061630L;

  /** The (internal) name of the report. */
  protected String m_name;
  /** Description of the report. */
  protected String m_description;
  /** Filter criteria on the data. */
  protected IFilter m_filter;
  /** Report parameters to be entered by the user. */
  protected List<ReportParameterDefinition> m_parameters;
  /** Fill missing data with transient entities (if applicable) */
  protected boolean m_fill;

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

  @XmlAttribute
  public boolean isFill()
  {
    return m_fill;
  }

  public void setFill(final boolean fill)
  {
    m_fill = fill;
  }

  @XmlElementRefs( { @XmlElementRef(type = AndFilter.class), @XmlElementRef(type = OrFilter.class),
      @XmlElementRef(type = DateRangeFilter.class), @XmlElementRef(type = ParameterRef.class),
      @XmlElementRef(type = PersonFilter.class), @XmlElementRef(type = TodoStateFilter.class),
      @XmlElementRef(type = TaskFilter.class), @XmlElementRef(type = ProjectFilter.class),
      @XmlElementRef(type = ProjectPathFilter.class) })
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

  /**
   * Get the entity type the report is based on.
   * 
   * E.g. WORKITEM will create a workitem report, DAYINFO a dayinfo report, ...
   * 
   * @return The entity type of the report.
   */
  @XmlTransient
  public abstract EntityType getEntityType();

  /**
   * Get the data collector.
   * 
   * @return The data collector.
   */
  public abstract IReportDataCollector createDataCollector();
}
