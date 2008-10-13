package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.sql.Date;
import java.util.Calendar;

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
    switch (entityType) {
      case DAYINFO:
        return "(d.date >= :from and d.date < :until)";
      case WORKITEM:
        return "(w.dayInfo.date >= :from and w.dayInfo.date < :until)";
      default:
        throw new RuntimeException("DateRangeFilter not applicable for entity type: " + entityType);
    }
  }

  public void setParameters(final EntityType entityType, final Query query)
  {
    query.setParameter("from", m_from);
    query.setParameter("until", m_until);
  }

  public static DateRangeFilter filterMonth(final int year, final int month)
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.set(year, month - 1, 1, 0, 0, 0);
    final Date from = new Date(calendar.getTimeInMillis());
    calendar.add(Calendar.MONTH, 1);
    final Date until = new Date(calendar.getTimeInMillis());

    return new DateRangeFilter(from, until);
  }
}
