package de.objectcode.time4u.server.api.filter;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.CalendarDay;

/**
 * Filter condition for dayinfo entities.
 * 
 * @author junglas
 */
@XmlType(name = "dayinfo-filter")
public class DayInfoFilter
{
  /** From calendar day (inclusively). */
  CalendarDay m_from;
  /** To calendar day (exclusively). */
  CalendarDay m_to;
  /** Minimum (inclusive) revision number (i.e. only revisions greater or equals are returned). */
  Long m_minRevision;
  /** Maximum (inclusive) revision number (i.e. only revisions less or equals are returned). */
  Long m_maxRevision;
  /** Client id of the last modification */
  Long m_lastModifiedByClient;

  public DayInfoFilter()
  {
  }

  public DayInfoFilter(final CalendarDay from, final CalendarDay to, final Long minRevision)
  {
    m_from = from;
    m_to = to;
    m_minRevision = minRevision;
  }

  public CalendarDay getFrom()
  {
    return m_from;
  }

  public void setFrom(final CalendarDay from)
  {
    m_from = from;
  }

  public CalendarDay getTo()
  {
    return m_to;
  }

  public void setTo(final CalendarDay to)
  {
    m_to = to;
  }

  public Long getMinRevision()
  {
    return m_minRevision;
  }

  public void setMinRevision(final Long minRevision)
  {
    m_minRevision = minRevision;
  }

  public Long getMaxRevision()
  {
    return m_maxRevision;
  }

  public void setMaxRevision(final Long maxRevision)
  {
    m_maxRevision = maxRevision;
  }

  public Long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final Long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  /**
   * Convenient method to create a "oll dayinfos of a given month" filter.
   * 
   * @param year
   *          The year
   * @param month
   *          The month (1-12)
   * @return The desired filter condition
   */
  public static DayInfoFilter filterMonth(final int year, final int month)
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.set(year, month - 1, 1, 0, 0, 0);
    final CalendarDay from = new CalendarDay(calendar);
    calendar.add(Calendar.MONTH, 1);
    final CalendarDay to = new CalendarDay(calendar);

    return new DayInfoFilter(from, to, null);
  }
}
