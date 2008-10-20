package de.objectcode.time4u.server.ejb.seam.api.report;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;

public abstract class BaseReportDefinition
{
  protected String m_name;
  protected String m_description;
  protected IFilter m_filter;

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

  public IFilter getFilter()
  {
    return m_filter;
  }

  public void setFilter(final IFilter filter)
  {
    m_filter = filter;
  }

  public abstract EntityType getEntityType();

  public abstract ReportResult createResult();

  public abstract void collect(final IRowDataAdapter rowData, final ReportResult reportResult);
}
