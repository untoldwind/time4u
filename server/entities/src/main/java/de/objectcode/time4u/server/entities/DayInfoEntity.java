package de.objectcode.time4u.server.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

/**
 * Day information entity.
 * 
 * This entity is related to the workitem entities as it provides a summary of workitems per day.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_DAYINFOS", uniqueConstraints = @UniqueConstraint(columnNames = { "person_clientId",
    "person_localId", "daydate" }))
public class DayInfoEntity
{
  /** Primary key */
  private EntityKey m_id;
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
  /** Set of workitem of this day. */
  private Set<WorkItemEntity> m_workItems;

  /**
   * Default constructor for hibernate.
   */
  protected DayInfoEntity()
  {
  }

  public DayInfoEntity(final EntityKey id, final long revision, final PersonEntity person, final Date date)
  {
    m_id = id;
    m_revision = revision;
    m_person = person;
    m_date = date;
    m_workItems = new HashSet<WorkItemEntity>();
    m_tags = new HashSet<DayTagEntity>();
  }

  @Id
  public EntityKey getId()
  {
    return m_id;
  }

  public void setId(final EntityKey id)
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
  @JoinColumns( { @JoinColumn(name = "person_clientId"), @JoinColumn(name = "person_localId") })
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
  @JoinTable(name = "T4U_DAYINFOS_TAGS", joinColumns = { @JoinColumn(name = "dayinfo_clientId"),
      @JoinColumn(name = "dayinfo_localId") }, inverseJoinColumns = { @JoinColumn(name = "tag_name") })
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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "dayInfo")
  public Set<WorkItemEntity> getWorkItems()
  {
    return m_workItems;
  }

  public void setWorkItems(final Set<WorkItemEntity> workItems)
  {
    m_workItems = workItems;
  }

  /**
   * Validate the dayinfo and all attached workitems.
   * 
   * Following checks are applied:
   * <ul>
   * <li>For every workitem: <tt>begin</tt> &gt;= 0 and <tt>end</tt> &gt;= 0</li>
   * <li>For every workitem: <tt>begin</tt> &lt;= 24 * 3600 and <tt>end</tt> &lt;= 24 * 3600</li>
   * <li>For every workitem: <tt>end</tt> &gt;= <tt>begin</tt></li>
   * <li>The <tt>begin</tt> and <tt>end</tt> intervals of all workitems of a day must not intersect with each other</li>
   * </ul>
   */
  public void validate()
  {
    int totalTime = 0;
    boolean hasInvalidWorkItems = false;

    for (final WorkItemEntity item1 : m_workItems) {
      if (item1.getBegin() < 0 || item1.getEnd() > 24 * 3600 || item1.getEnd() < item1.getBegin()) {
        item1.setValid(false);
        hasInvalidWorkItems = true;
      } else {
        item1.setValid(true);
        for (final WorkItemEntity item2 : m_workItems) {
          if (item1.getId() != item2.getId()) {
            if (item1.getBegin() > item2.getBegin() && item1.getBegin() < item2.getEnd()) {
              item1.setValid(false);
              hasInvalidWorkItems = true;
            } else if (item1.getEnd() > item2.getBegin() && item1.getEnd() < item2.getEnd()) {
              item1.setValid(false);
              hasInvalidWorkItems = true;
            }
          }
        }

        if (item1.isValid()) {
          totalTime += item1.getDuration();
        }
      }
    }

    m_hasWorkItems = !m_workItems.isEmpty();
    m_hasInvalidWorkItems = hasInvalidWorkItems;
    m_totalTime = totalTime;
  }

  public void toSummaryDTO(final DayInfoSummary dayinfo)
  {
    dayinfo.setId(m_id.getUUID());
    dayinfo.setRevision(m_revision);
    dayinfo.setDay(new CalendarDay(m_date));
    dayinfo.setHasWorkItems(m_hasWorkItems);
    dayinfo.setHasInvalidWorkItems(m_hasInvalidWorkItems);
    dayinfo.setRegularTime(m_regularTime);
  }

  public void toDTO(final DayInfo dayinfo)
  {
    toSummaryDTO(dayinfo);

    if (m_workItems != null) {
      final List<WorkItem> workItems = new ArrayList<WorkItem>();
      for (final WorkItemEntity entity : m_workItems) {
        final WorkItem workItem = new WorkItem();

        entity.toDTO(workItem);

        workItems.add(workItem);
      }
      Collections.sort(workItems);
      dayinfo.setWorkItems(workItems);
    } else {
      final List<WorkItem> workItems = Collections.emptyList();

      dayinfo.setWorkItems(workItems);
    }
  }
}
