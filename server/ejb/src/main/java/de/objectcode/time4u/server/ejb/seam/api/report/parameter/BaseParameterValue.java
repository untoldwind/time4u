package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

import java.io.Serializable;

import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterType;

public abstract class BaseParameterValue implements Serializable
{
  private static final long serialVersionUID = -7239346656523879010L;

  protected final String m_name;
  protected final String m_label;
  protected final ReportParameterType m_type;

  protected BaseParameterValue(final String name, final String label, final ReportParameterType type)
  {
    m_name = name;
    m_label = label;
    m_type = type;
  }

  public String getName()
  {
    return m_name;
  }

  public String getLabel()
  {
    return m_label;
  }

  public ReportParameterType getType()
  {
    return m_type;
  }

  public abstract IFilter getFilter();
}
