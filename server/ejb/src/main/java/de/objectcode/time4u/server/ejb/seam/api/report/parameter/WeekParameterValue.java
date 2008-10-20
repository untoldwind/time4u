package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

import java.util.Calendar;

import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterType;

public class WeekParameterValue extends BaseParameterValue
{
  private static final long serialVersionUID = -1930348391070749128L;

  private int m_week;
  private int m_year;

  public WeekParameterValue(final String name, final String label)
  {
    super(name, label, ReportParameterType.WEEK);

    final Calendar calendar = Calendar.getInstance();

    m_week = calendar.get(Calendar.WEEK_OF_YEAR);
    m_year = calendar.get(Calendar.YEAR);
  }

  public WeekParameterValue(final String name, final String label, final int year, final int week)
  {
    super(name, label, ReportParameterType.WEEK);

    m_week = week;
    m_year = year;
  }

  public int getWeek()
  {
    return m_week;
  }

  public int getYear()
  {
    return m_year;
  }

  public void setWeek(final int week)
  {
    m_week = week;
  }

  public void setYear(final int year)
  {
    m_year = year;
  }

  @Override
  public IFilter getFilter()
  {
    return DateRangeFilter.filterWeek(m_year, m_week);
  }

}
