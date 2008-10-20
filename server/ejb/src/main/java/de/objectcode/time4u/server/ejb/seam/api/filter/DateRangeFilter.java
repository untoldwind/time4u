package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.sql.Date;
import java.util.Calendar;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.io.DateXmlAdatper;

@XmlType(name = "date-range")
@XmlRootElement(name = "date-range")
public class DateRangeFilter implements IFilter
{
  private Date m_from;
  private Date m_until;

  public DateRangeFilter()
  {
  }

  public DateRangeFilter(final Date from, final Date until)
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
