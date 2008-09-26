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

@Entity
@Table(name = "T4U_ACTIVE_WORKITEMS")
public class ActiveWorkItemEntity
{
  /** Primary key. */
  private long m_id;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  private PersonEntity m_person;
  private WorkItemEntity m_workItem;

  /**
   * Default constructor for hibernate.
   */
  protected ActiveWorkItemEntity()
  {
  }

  public ActiveWorkItemEntity(final long revision, final PersonEntity person, final WorkItemEntity workItem)
  {
    m_person = person;
    m_revision = revision;
    m_workItem = workItem;
  }

  @Id
  @GeneratedValue(generator = "PERSON_ID")
  @GenericGenerator(name = "PERSON_ID", strategy = "foreign", parameters = { @Parameter(name = "property", value = "person") })
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

  @ManyToOne
  @JoinColumn(name = "workitem_id", nullable = true)
  public WorkItemEntity getWorkItem()
  {
    return m_workItem;
  }

  public void setWorkItem(final WorkItemEntity workItem)
  {
    m_workItem = workItem;
  }
}
