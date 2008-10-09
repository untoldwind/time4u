package de.objectcode.time4u.server.api.data;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * A (gregorian) calendar day.
 * 
 * @author junglas
 */
@XmlType(name = "calendar-day")
public class CalendarDay implements Serializable, Comparable<CalendarDay>
{
  private static final long serialVersionUID = 5239789740978583628L;

  private int m_day;
  private int m_month;
  private int m_year;

  public CalendarDay()
  {
  }

  public CalendarDay(final int day, final int month, final int year)
  {
    m_day = day;
    m_month = month;
    m_year = year;
  }

  public CalendarDay(final Date date)
  {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    m_day = calendar.get(Calendar.DAY_OF_MONTH);
    m_month = calendar.get(Calendar.MONTH) + 1;
    m_year = calendar.get(Calendar.YEAR);
  }

  public CalendarDay(final Calendar calendar)
  {
    m_day = calendar.get(Calendar.DAY_OF_MONTH);
    m_month = calendar.get(Calendar.MONTH) + 1;
    m_year = calendar.get(Calendar.YEAR);
  }

  @XmlAttribute
  public int getDay()
  {
    return m_day;
  }

  @XmlAttribute
  public int getMonth()
  {
    return m_month;
  }

  @XmlAttribute
  public int getYear()
  {
    return m_year;
  }

  public void setDay(final int day)
  {
    m_day = day;
  }

  public void setMonth(final int month)
  {
    m_month = month;
  }

  public void setYear(final int year)
  {
    m_year = year;
  }

  @XmlTransient
  public Calendar getCalendar()
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.set(m_year, m_month - 1, m_day, 0, 0, 0);

    return calendar;
  }

  @XmlTransient
  public Date getDate()
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.set(m_year, m_month - 1, m_day, 0, 0, 0);

    return new Date(calendar.getTimeInMillis());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return m_day + 12 * (32 * m_month + m_year);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)

  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof CalendarDay)) {
      return false;
    }

    final CalendarDay castObj = (CalendarDay) obj;

    return m_day == castObj.m_day && m_month == castObj.m_month && m_year == castObj.m_year;
  }

  /**
   * {@inheritDoc}
   */
  public int compareTo(final CalendarDay o)
  {
    if (m_year != o.m_year) {
      return m_year < o.m_year ? -1 : 1;
    }

    if (m_month != o.m_month) {
      return m_month < o.m_month ? -1 : 1;
    }

    if (m_day != o.m_day) {
      return m_day < o.m_day ? -1 : 1;
    }

    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("CalendarDay(");
    buffer.append("year=").append(m_year);
    buffer.append(", month=").append(m_month);
    buffer.append(", day=").append(m_day);
    buffer.append(")");
    return buffer.toString();
  }
}
