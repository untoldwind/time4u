package de.objectcode.time4u.server.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Stores the currently active workitem of a person.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_ACTIVE_WORKITEMS")
public class ActiveWorkItemEntity
{
  /** Primary key. */
  private String m_id;
  /** Revision number (increased every time something has changed). */
  private long m_revision;
  /** Client id of the last modification. */
  private long m_lastModifiedByClient;
  private PersonEntity m_person;
  private WorkItemEntity m_workItem;

  /**
   * Default constructor for hibernate.
   */
  protected ActiveWorkItemEntity()
  {
  }

  /**
   * Standard constructor.
   * 
   * @param revision
   *          The revision number
   * @param person
   *          The person the active workitem belongs to
   * @param workItem
   *          The active workitem
   */
  public ActiveWorkItemEntity(final long revision, final PersonEntity person, final WorkItemEntity workItem)
  {
    m_person = person;
    m_revision = revision;
    m_workItem = workItem;
  }

  @Id
  @GeneratedValue(generator = "PERSON_ID")
  @GenericGenerator(name = "PERSON_ID", strategy = "foreign", parameters = {
    @Parameter(name = "property", value = "person")
  })
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

  @OneToOne
  @PrimaryKeyJoinColumn
  public PersonEntity getPerson()
  {
    return m_person;
  }

  public void setPerson(final PersonEntity person)
  {
    m_person = person;
  }

  @ManyToOne(optional = true)
  @JoinColumn(name = "workitem_id")
  public WorkItemEntity getWorkItem()
  {
    return m_workItem;
  }

  public void setWorkItem(final WorkItemEntity workItem)
  {
    m_workItem = workItem;
  }
}
