package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlType;

/**
 * Abstract base class of all time-policy.
 * 
 * @author junglas
 */
@XmlType(name = "time-policy")
public abstract class TimePolicy implements ISynchronizableData
{
  private static final long serialVersionUID = 5344090291753057165L;

  /** Internal server id of the team. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Flag if the timepolicy is deleted */
  private boolean m_deleted;
  protected CalendarDay m_validFrom;
  protected CalendarDay m_validUntil;

  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
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

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
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
