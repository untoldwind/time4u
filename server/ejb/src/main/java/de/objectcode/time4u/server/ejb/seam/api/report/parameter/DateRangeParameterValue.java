package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

import java.util.Calendar;
import java.util.Date;

import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterType;

public class DateRangeParameterValue extends BaseParameterValue
{
  private static final long serialVersionUID = -6725443755966097171L;

  private Date m_from;
  private Date m_until;

  public DateRangeParameterValue(final String name, final String label)
  {
    super(name, label, ReportParameterType.DATE_RANGE);

    final Calendar calendar = Calendar.getInstance();

    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    m_from = calendar.getTime();
    calendar.add(Calendar.MONTH, 1);
    m_until = calendar.getTime();
  }

  public Date getFrom()
  {
    return m_from;
  }

  public Date getUntil()
  {
    return m_until;
  }

  public void setFrom(final Date from)
  {
    m_from = from;
  }

  public void setUntil(final Date until)
  {
    m_until = until;
  }

  @Override
  public IFilter getFilter()
  {
    return new DateRangeFilter(m_from, m_until);
  }
}
