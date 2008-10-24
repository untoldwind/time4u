package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

import java.util.Calendar;

import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterType;

public class MonthParameterValue extends BaseParameterValue
{
  private static final long serialVersionUID = 6561044423596897457L;

  private int m_month;
  private int m_year;

  public MonthParameterValue(final String name, final String label)
  {
    super(name, label, ReportParameterType.MONTH);

    final Calendar calendar = Calendar.getInstance();

    m_month = calendar.get(Calendar.MONTH) + 1;
    m_year = calendar.get(Calendar.YEAR);
  }

  public MonthParameterValue(final String name, final String label, final int year, final int month)
  {
    super(name, label, ReportParameterType.MONTH);

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

  public MonthEnum[] getMonthValues()
  {
    return MonthEnum.values();
  }

  @Override
  public IFilter getFilter()
  {
    return DateRangeFilter.filterMonth(m_year, m_month);
  }
}
