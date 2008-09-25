package de.objectcode.time4u.server.api.filter;

import de.objectcode.time4u.server.api.data.CalendarDay;

public class DayInfoFilter
{
  /** From calendar day. */
  CalendarDay m_from;
  /** To calendar day. */
  CalendarDay m_to;
  /** Minimum revision number (i.e. only revisions greater or equals are returned). */
  Long m_minRevision;

  public CalendarDay getFrom()
  {
    return m_from;
  }

  public void setFrom(final CalendarDay from)
  {
    m_from = from;
  }

  public CalendarDay getTo()
  {
    return m_to;
  }

  public void setTo(final CalendarDay to)
  {
    m_to = to;
  }

  public Long getMinRevision()
  {
    return m_minRevision;
  }

  public void setMinRevision(final Long minRevision)
  {
    m_minRevision = minRevision;
  }
}
