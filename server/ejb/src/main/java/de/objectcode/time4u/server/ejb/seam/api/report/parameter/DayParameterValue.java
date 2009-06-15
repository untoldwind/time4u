package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

import java.util.Calendar;
import java.util.Date;

import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterType;

public class DayParameterValue extends BaseParameterValue
{
  private static final long serialVersionUID = -4924497643345268267L;
  private Date m_date;

  public DayParameterValue(final String name, final String label)
  {
    super(name, label, ReportParameterType.DAY);

    final Calendar calendar = Calendar.getInstance();

    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    m_date = calendar.getTime();
  }

  public Date getDate()
  {
    return m_date;
  }

  public void setDate(final Date date)
  {
    m_date = date;
  }

  public Date getFrom()
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.setTime(m_date);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public Date getUntil()
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.setTime(m_date);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    return calendar.getTime();
  }

  @Override
  public IFilter getFilter()
  {
    return new DateRangeFilter(getFrom(), getUntil());
  }
}
