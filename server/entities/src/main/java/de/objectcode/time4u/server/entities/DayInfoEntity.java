package de.objectcode.time4u.server.entities;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Day information entity.
 * 
 * This entity is related to the workitem entities as it provides a summary of workitems per day.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_DAYINFOS")
public class DayInfoEntity
{
  /** Primary key */
  private long m_id;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** The person this day belongs too. */
  private PersonEntity m_person;
  /** Date of the day */
  private Date m_date;
  private boolean m_hasWorkItems;
  private boolean m_hasInvalidWorkItems;
  private int m_totalTime;
  private int m_regularTime;

  /** Set of tags of the day */
  private Set<DayTagEntity> m_tags;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_DAYINFOS")
  @GenericGenerator(name = "SEQ_T4U_DAYINFOS", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_DAYINFOS"))
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

  @ManyToOne
  @JoinColumn(name = "person_id", nullable = false)
  public PersonEntity getPerson()
  {
    return m_person;
  }

  public void setPerson(final PersonEntity person)
  {
    m_person = person;
  }

  @Column(name = "daydate", nullable = false)
  public Date getDate()
  {
    return m_date;
  }

  public void setDate(final Date date)
  {
    m_date = date;
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "T4U_DAYINFOS_TAGS", joinColumns = { @JoinColumn(name = "dayinf_id") }, inverseJoinColumns = { @JoinColumn(name = "tag_name") })
  public Set<DayTagEntity> getTags()
  {
    return m_tags;
  }

  public void setTags(final Set<DayTagEntity> tags)
  {
    m_tags = tags;
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

  public int getTotalTime()
  {
    return m_totalTime;
  }

  public void setTotalTime(final int totalTime)
  {
    m_totalTime = totalTime;
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
