package de.objectcode.time4u.server.api.data;

import java.util.UUID;

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
  private UUID m_id;
  /** Revision number. */
  private long m_revision;
  /** The calendar day of the dayinfo. */
  private CalendarDay m_day;
  /** Flag if the day has any workitems */
  private boolean m_hasWorkItems;
  /** Flag if the day has invalid workitems */
  private boolean m_hasInvalidWorkItems;
  /** The regular working time for the day (usually calculated by time policies) */
  private int m_regularTime;

  public UUID getId()
  {
    return m_id;
  }

  public void setId(final UUID id)
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

  public boolean isHasWorkItems()
  {
    return m_hasWorkItems;
  }

  public void setHasWorkItems(final boolean hasWorkItems)
  {
    m_hasWorkItems = hasWorkItems;
  }

  public boolean isHasInvalidWorkItems()
  {
    return m_hasInvalidWorkItems;
  }

  public void setHasInvalidWorkItems(final boolean hasInvalidWorkItems)
  {
    m_hasInvalidWorkItems = hasInvalidWorkItems;
  }

  public int getRegularTime()
  {
    return m_regularTime;
  }

  public void setRegularTime(final int regularTime)
  {
    m_regularTime = regularTime;
  }

}
