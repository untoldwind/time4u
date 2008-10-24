package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * DayInfo DTO object.
 * 
 * This DTO only transfers the summary of a day info.
 * 
 * @author junglas
 */
@XmlType(name = "day-info-summary")
@XmlRootElement(name = "day-info-summary")
public class DayInfoSummary implements ISynchronizableData
{
  private static final long serialVersionUID = 4834724509315672372L;

  /** Internal server id of the dayinfo. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** The calendar day of the dayinfo. */
  private CalendarDay m_day;
  /** Flag if the day has any workitems */
  private boolean m_hasWorkItems;
  /** Flag if the day has invalid workitems */
  private boolean m_hasInvalidWorkItems;
  /** Flag if the day has tags */
  private boolean m_hasTags;
  /** The regular working time for the day (usually calculated by time policies) */
  private int m_regularTime;
  /** Sum of all workitem durations or that day */
  private int m_sumDurations;

  @XmlAttribute
  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
  {
    m_id = id;
  }

  @XmlAttribute
  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  @XmlAttribute
  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public CalendarDay getDay()
  {
    return m_day;
  }

  public void setDay(final CalendarDay day)
  {
    m_day = day;
  }

  @XmlAttribute
  public boolean isHasWorkItems()
  {
    return m_hasWorkItems;
  }

  public void setHasWorkItems(final boolean hasWorkItems)
  {
    m_hasWorkItems = hasWorkItems;
  }

  @XmlAttribute
  public boolean isHasInvalidWorkItems()
  {
    return m_hasInvalidWorkItems;
  }

  public void setHasInvalidWorkItems(final boolean hasInvalidWorkItems)
  {
    m_hasInvalidWorkItems = hasInvalidWorkItems;
  }

  @XmlAttribute
  public int getRegularTime()
  {
    return m_regularTime;
  }

  public void setRegularTime(final int regularTime)
  {
    m_regularTime = regularTime;
  }

  @XmlAttribute
  public int getSumDurations()
  {
    return m_sumDurations;
  }

  public void setSumDurations(final int sumDurations)
  {
    m_sumDurations = sumDurations;
  }

  @XmlAttribute
  public boolean isHasTags()
  {
    return m_hasTags;
  }

  public void setHasTags(final boolean hasTags)
  {
    m_hasTags = hasTags;
  }
}
