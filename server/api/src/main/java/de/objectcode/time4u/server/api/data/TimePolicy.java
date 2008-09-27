package de.objectcode.time4u.server.api.data;

import java.util.UUID;

public abstract class TimePolicy implements ISynchronizableData
{
  private static final long serialVersionUID = 5344090291753057165L;

  /** Internal server id of the team. */
  private UUID m_id;
  /** Revision number. */
  private long m_revision;
  protected CalendarDay m_validFrom;
  protected CalendarDay m_validUntil;

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

  public CalendarDay getValidFrom()
  {
    return m_validFrom;
  }

  public void setValidFrom(final CalendarDay validFrom)
  {
    m_validFrom = validFrom;
  }

  public CalendarDay getValidUntil()
  {
    return m_validUntil;
  }

  public void setValidUntil(final CalendarDay validUntil)
  {
    m_validUntil = validUntil;
  }

  public abstract int getRegularTime(CalendarDay day);
}
