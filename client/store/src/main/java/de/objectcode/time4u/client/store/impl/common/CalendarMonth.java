package de.objectcode.time4u.client.store.impl.common;

import java.io.Serializable;
import java.util.Calendar;

import de.objectcode.time4u.server.api.data.CalendarDay;

/**
 * A calendar month.
 * 
 * @author junglas
 */
public class CalendarMonth implements Serializable, Comparable<CalendarMonth>
{
  private static final long serialVersionUID = 4081169151982855523L;

  private int m_month;
  private int m_year;

  public CalendarMonth()
  {
  }

  public CalendarMonth(final int month, final int year)
  {
    m_month = month;
    m_year = year;
  }

  public CalendarMonth(final CalendarDay day)
  {
    m_month = day.getMonth();
    m_year = day.getYear();
  }

  public CalendarMonth(final CalendarMonth month)
  {
    m_month = month.getMonth();
    m_year = month.getYear();
  }

  public CalendarMonth(final Calendar calendar)
  {
    m_month = calendar.get(Calendar.MONTH) + 1;
    m_year = calendar.get(Calendar.YEAR);
  }

  public int getMonth()
  {
    return m_month;
  }

  public int getYear()
  {
    return m_year;
  }

  public void setMonth(final int month)
  {
    m_month = month;
  }

  public void setYear(final int year)
  {
    m_year = year;
  }

  @Override
  public int hashCode()
  {
    return 12 * m_year + m_month;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }
    if (obj == null || !(obj instanceof CalendarMonth)) {
      return false;
    }

    final CalendarMonth castObj = (CalendarMonth) obj;

    return m_month == castObj.m_month && m_year == castObj.m_year;
  }

  public int compareTo(final CalendarMonth o)
  {
    if (m_year != o.m_year) {
      return m_year < o.m_year ? -1 : 1;
    }
    if (m_month != o.m_month) {
      return m_month < o.m_month ? -1 : 1;
    }

    return 0;
  }

  @Override
  public String toString()
  {
    return "CalendarMonth(" + m_month + "." + m_year + ")";
  }
}
