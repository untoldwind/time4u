package de.objectcode.time4u.server.api.data;

import java.util.Calendar;

public class WeekTimePolicy extends TimePolicy
{
  private static final long serialVersionUID = -3455841618528168636L;

  private int m_mondayTime;
  private int m_tuesdayTime;
  private int m_wednesdayTime;
  private int m_thursdayTime;
  private int m_fridayTime;
  private int m_saturdayTime;
  private int m_sundayTime;

  public int getFridayTime()
  {
    return m_fridayTime;
  }

  public void setFridayTime(final int fridayTime)
  {
    m_fridayTime = fridayTime;
  }

  public int getMondayTime()
  {
    return m_mondayTime;
  }

  public void setMondayTime(final int mondayTime)
  {
    m_mondayTime = mondayTime;
  }

  public int getSaturdayTime()
  {
    return m_saturdayTime;
  }

  public void setSaturdayTime(final int saturdayTime)
  {
    m_saturdayTime = saturdayTime;
  }

  public int getSundayTime()
  {
    return m_sundayTime;
  }

  public void setSundayTime(final int sundayTime)
  {
    m_sundayTime = sundayTime;
  }

  public int getThursdayTime()
  {
    return m_thursdayTime;
  }

  public void setThursdayTime(final int thursdayTime)
  {
    m_thursdayTime = thursdayTime;
  }

  public int getTuesdayTime()
  {
    return m_tuesdayTime;
  }

  public void setTuesdayTime(final int tuesdayTime)
  {
    m_tuesdayTime = tuesdayTime;
  }

  public int getWednesdayTime()
  {
    return m_wednesdayTime;
  }

  public void setWednesdayTime(final int wednesdayTime)
  {
    m_wednesdayTime = wednesdayTime;
  }

  @Override
  public int getRegularTime(final CalendarDay day)
  {
    if (m_validFrom != null && m_validFrom.compareTo(day) > 0) {
      return -1;
    }
    if (m_validUntil != null && m_validUntil.compareTo(day) < 0) {
      return -1;
    }

    final Calendar calendar = day.getCalendar();

    switch (calendar.get(Calendar.DAY_OF_WEEK)) {
      case Calendar.MONDAY:
        return m_mondayTime;
      case Calendar.TUESDAY:
        return m_tuesdayTime;
      case Calendar.WEDNESDAY:
        return m_wednesdayTime;
      case Calendar.THURSDAY:
        return m_thursdayTime;
      case Calendar.FRIDAY:
        return m_fridayTime;
      case Calendar.SATURDAY:
        return m_saturdayTime;
      case Calendar.SUNDAY:
        return m_sundayTime;
    }
    return -1;
  }

}
