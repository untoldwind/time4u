package de.objectcode.time4u.server.web.ui.user;

import java.util.Calendar;

public class MonthBean
{
  private int m_month;
  private int m_year;

  public MonthBean()
  {
    final Calendar calendar = Calendar.getInstance();

    m_month = calendar.get(Calendar.MONTH) + 1;
    m_year = calendar.get(Calendar.YEAR);
  }

  public MonthBean(final int month, final int year)
  {
    m_month = month;
    m_year = year;
  }

  public int getMonth()
  {
    return m_month;
  }

  public void setMonth(final int month)
  {
    m_month = month;
  }

  public int getYear()
  {
    return m_year;
  }

  public void setYear(final int year)
  {
    m_year = year;
  }

}
