package de.objectcode.time4u.server.ejb.seam.api.report;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.filter.AndFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.OrFilter;

@XmlType(name = "report")
public abstract class BaseReportDefinition
{
  protected String m_name;
  protected String m_description;
  protected IFilter m_filter;

  @XmlAttribute
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @XmlElementRefs( { @XmlElementRef(type = AndFilter.class), @XmlElementRef(type = OrFilter.class),
      @XmlElementRef(type = DateRangeFilter.class) })
  public IFilter getFilter()
  {
    return m_filter;
  }

  public void setFilter(final IFilter filter)
  {
    m_filter = filter;
  }

  @XmlTransient
  public abstract EntityType getEntityType();

  public abstract ReportResult createResult();

  public abstract void collect(final IRowDataAdapter rowData, final ReportResult reportResult);
}
