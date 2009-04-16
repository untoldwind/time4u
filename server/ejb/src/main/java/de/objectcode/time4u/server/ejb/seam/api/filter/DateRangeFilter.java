package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.sql.Date;
import java.util.Calendar;
import java.util.Map;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.io.DateXmlAdatper;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

@XmlType(name = "date-range")
@XmlRootElement(name = "date-range")
public class DateRangeFilter implements IFilter
{
  private static final long serialVersionUID = 7407579906174361131L;

  private Date m_from;
  private Date m_until;

  public DateRangeFilter()
  {
  }

  public DateRangeFilter(final java.util.Date from, final java.util.Date until)
  {
    m_from = new Date(from.getTime());
    m_until = new Date(until.getTime());
  }

  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters)
  {
    switch (entityType) {
      case DAYINFO:
        return "(d.date >= :from and d.date < :until)";
      case WORKITEM:
        return "(w.dayInfo.date >= :from and w.dayInfo.date < :until)";
      case TODO:
        return "(t.createdAt >= :from and t.createdAt < :until)";
      default:
        throw new RuntimeException("DateRangeFilter not applicable for entity type: " + entityType);
    }
  }

  @XmlJavaTypeAdapter(DateXmlAdatper.class)
  @XmlAttribute
  public Date getFrom()
  {
    return m_from;
  }

  public void setFrom(final Date from)
  {
    m_from = from;
  }

  @XmlJavaTypeAdapter(DateXmlAdatper.class)
  @XmlAttribute
  public Date getUntil()
  {
    return m_until;
  }

  public void setUntil(final Date until)
  {
    m_until = until;
  }

  public void setQueryParameters(final EntityType entityType, final Query query,
      final Map<String, BaseParameterValue> parameters)
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

  public static DateRangeFilter filterWeek(final int year, final int week)
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.set(year, 0, 1, 0, 0, 0);
    calendar.set(Calendar.WEEK_OF_YEAR, week);
    final Date from = new Date(calendar.getTimeInMillis());
    calendar.add(Calendar.WEEK_OF_YEAR, 1);
    final Date until = new Date(calendar.getTimeInMillis());

    return new DateRangeFilter(from, until);
  }
}
