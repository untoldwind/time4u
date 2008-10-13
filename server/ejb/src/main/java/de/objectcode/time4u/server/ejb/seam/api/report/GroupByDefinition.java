package de.objectcode.time4u.server.ejb.seam.api.report;

public class GroupByDefinition
{
  IProjection m_projection;

  public IProjection getProjection()
  {
    return m_projection;
  }

  public void setProjection(final IProjection projection)
  {
    m_projection = projection;
  }

}
