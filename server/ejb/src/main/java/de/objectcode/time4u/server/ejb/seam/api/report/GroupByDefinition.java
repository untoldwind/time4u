package de.objectcode.time4u.server.ejb.seam.api.report;

public class GroupByDefinition
{
  IProjection m_valueProjection;
  IProjection m_labelProjection;

  public GroupByDefinition(final IProjection valueProjection, final IProjection labelProjection)
  {
    m_valueProjection = valueProjection;
    m_labelProjection = labelProjection;
  }

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

  public ValueLabelPair project(final IRowDataAdapter rowData)
  {
    return new ValueLabelPair(m_valueProjection.project(rowData), m_labelProjection.project(rowData));
  }
}
