package de.objectcode.time4u.server.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import de.objectcode.time4u.server.entities.context.IPersistenceContext;

/**
 * Day information entity.
 * 
 * This entity is related to the workitem entities as it provides a summary of workitems per day.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_DAYINFOS", uniqueConstraints = @UniqueConstraint(columnNames = { "person_id", "daydate" }))
public class DayInfoEntity
{
  /** Primary key */
  private String m_id;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
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

  public DayInfoEntity(final String id, final long revision, final long lastModifiedByClient,
      final PersonEntity person, final Date date)
  {
    m_id = id;
    m_revision = revision;
    m_lastModifiedByClient = lastModifiedByClient;
    m_person = person;
    m_date = date;
    m_regularTime = -1;
    m_workItems = new HashSet<WorkItemEntity>();
    m_tags = new HashSet<DayTagEntity>();
  }

  @Id
  @Column(length = 36)
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

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  @ManyToOne
  @JoinColumn(name = "person_id")
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
  @JoinTable(name = "T4U_DAYINFOS_TAGS", joinColumns = { @JoinColumn(name = "dayinfo_id") }, inverseJoinColumns = { @JoinColumn(name = "tag_name") })
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

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "dayInfo")
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
          if (!item1.getId().equals(item2.getId())) {
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
    dayinfo.setId(m_id);
    dayinfo.setRevision(m_revision);
    dayinfo.setLastModifiedByClient(m_lastModifiedByClient);
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

  public void fromDTO(final IPersistenceContext context, final DayInfo dayInfo)
  {
    m_lastModifiedByClient = dayInfo.getLastModifiedByClient();
    m_regularTime = dayInfo.getRegularTime();

    final Set<String> workItemIds = new HashSet<String>();

    for (final WorkItem workItem : dayInfo.getWorkItems()) {
      final WorkItemEntity workItemEntity = new WorkItemEntity(workItem.getId(), this);

      workItemEntity.fromDTO(context, workItem);

      context.merge(workItemEntity);
      workItemIds.add(workItem.getId());
    }
    final Iterator<WorkItemEntity> it = m_workItems.iterator();
    while (it.hasNext()) {
      final WorkItemEntity entity = it.next();

      if (!workItemIds.contains(entity.getId())) {
        context.delete(entity);
        it.remove();
      }
    }
  }
}
