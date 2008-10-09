package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.sql.Date;

import javax.persistence.Query;

import de.objectcode.time4u.server.api.data.EntityType;

public class DateRangeFilter implements IFilter
{
  private final Date m_from;
  private final Date m_until;

  private DateRangeFilter(final Date from, final Date until)
  {
    m_from = from;
    m_until = until;
  }

  public String getWhereClause(final EntityType entityType)
  {
    return "(d.date >= :from and d.date < :until)";
  }

  public String getWorkItemWhereClause()
  {
    return "(w.dayInfo.date >= :from and w.dayInfo.date < :until)";
  }

  public void setParameters(final EntityType entityType, final Query query)
  {
    query.setParameter("from", m_from);
    query.setParameter("until", m_until);
  }

}
