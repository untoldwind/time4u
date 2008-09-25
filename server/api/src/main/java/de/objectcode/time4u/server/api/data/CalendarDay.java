package de.objectcode.time4u.server.api.data;

import java.io.Serializable;

/**
 * A (gregorian) calendar day.
 * 
 * @author junglas
 */
public class CalendarDay implements Serializable
{
  private static final long serialVersionUID = 5239789740978583628L;

  private int m_day;
  private int m_month;
  private int m_year;

  public CalendarDay(final int day, final int month, final int year)
  {
    m_day = day;
    m_month = month;
    m_year = year;
  }

  public int getDay()
  {
    return m_day;
  }

  public int getMonth()
  {
    return m_month;
  }

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
}
