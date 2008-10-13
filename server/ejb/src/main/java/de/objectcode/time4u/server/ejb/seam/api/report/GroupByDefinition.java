package de.objectcode.time4u.server.ejb.seam.api.report;

public class GroupByDefinition
{
  IProjection m_valueProjection;
  IProjection m_labelProjection;

  public IProjection getValueProjection()
  {
    return m_valueProjection;
  }

  public void setValueProjection(final IProjection valueProjection)
  {
    m_valueProjection = valueProjection;
  }

  public IProjection getLabelProjection()
  {
    return m_labelProjection;
  }

  public void setLabelProjection(final IProjection labelProjection)
  {
    m_labelProjection = labelProjection;
  }
}
