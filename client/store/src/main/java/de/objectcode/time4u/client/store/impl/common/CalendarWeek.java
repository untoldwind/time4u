package de.objectcode.time4u.client.store.impl.common;

import java.io.Serializable;
import java.util.Calendar;

import de.objectcode.time4u.server.api.data.CalendarDay;

/**
 * A calendar week.
 * 
 * @author junglas
 */
public class CalendarWeek implements Serializable, Comparable<CalendarWeek>
{
  private static final long serialVersionUID = -5009575330276189191L;

  private int m_week;
  private int m_year;

  public CalendarWeek()
  {
  }

  public CalendarWeek(final CalendarDay day)
  {
    final Calendar calendar = day.getCalendar();

    m_week = calendar.get(Calendar.WEEK_OF_YEAR);
    m_year = calendar.get(Calendar.YEAR);
  }

  public CalendarWeek(final int week, final int year)
  {
    m_week = week;
    m_year = year;
  }

  @Override
  public int hashCode()
  {
    return 54 * m_year + m_week;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof CalendarWeek)) {
      return false;
    }

    final CalendarWeek castObj = (CalendarWeek) obj;

    return m_week == castObj.m_week && m_year == castObj.m_year;
  }

  public int compareTo(final CalendarWeek o)
  {
    if (m_year != o.m_year) {
      return m_year < o.m_year ? -1 : 1;
    }
    if (m_week != o.m_week) {
      return m_week < o.m_week ? -1 : 1;
    }

    return 0;
  }

  @Override
  public String toString()
  {
    return "CalendarWeek(" + m_week + "." + m_year + ")";
  }
}
