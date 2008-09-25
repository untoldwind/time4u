package de.objectcode.time4u.server.api.data;


/**
 * DayInfo DTO object.
 * 
 * This DTO only transfers the summary of a day info.
 * 
 * @author junglas
 */
public class DayInfoSummary implements ISynchronizableData
{
  private static final long serialVersionUID = 4834724509315672372L;

  /** Internal server id of the dayinfo. */
  private long m_id;
  /** Revision number. */
  private long m_revision;
  /** The calendar day of the dayinfo. */
  private CalendarDay m_day;

  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  public CalendarDay getDay()
  {
    return m_day;
  }

  public void setDay(final CalendarDay day)
  {
    m_day = day;
  }
}
