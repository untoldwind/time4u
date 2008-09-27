package de.objectcode.time4u.server.entities.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.entities.PersonEntity;

/**
 * Person history entity. This is part of the audit information.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_PERSONS_HISTORY")
public class PersonHistoryEntity
{
  /** Primary key. */
  private long m_id;
  /** The person this entity belongs to. */
  private PersonEntity m_person;
  /** The person who performed the change. */
  private PersonEntity m_performedBy;
  /** Timestamp of the change. */
  private Date m_performedAt;
  /** Original name of the person. */
  private String m_name;
  /** Original email of the person. */
  private String m_email;

  public PersonHistoryEntity()
  {
  }

  public PersonHistoryEntity(final PersonEntity person, final PersonEntity performedBy)
  {
    m_person = person;
    m_performedBy = performedBy;
    m_performedAt = new Date();

    m_name = person.getName();
    m_email = person.getEmail();
  }

  @Id
  @GeneratedValue(generator = "SEQ_T4U_PERSONS_HISTORY")
  @GenericGenerator(name = "SEQ_T4U_PERSONS_HISTORY", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_PERSONS_HISTORY"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @Column(length = 50, nullable = false)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Column(length = 200, nullable = true)
  public String getEmail()
  {
    return m_email;
  }

  public void setEmail(final String email)
  {
    m_email = email;
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

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "performedBy_clientId"), @JoinColumn(name = "performedBy_localId") })
  public PersonEntity getPerformedBy()
  {
    return m_performedBy;
  }

  public void setPerformedBy(final PersonEntity performedBy)
  {
    m_performedBy = performedBy;
  }

  @Column(nullable = false)
  public Date getPerformedAt()
  {
    return m_performedAt;
  }

  public void setPerformedAt(final Date performedAt)
  {
    m_performedAt = performedAt;
  }
}
