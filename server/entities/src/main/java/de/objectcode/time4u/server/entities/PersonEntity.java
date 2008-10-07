package de.objectcode.time4u.server.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.Person;

/**
 * Person entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_PERSONS")
public class PersonEntity
{
  /** Primary key. */
  private String m_id;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Given name of the person. */
  private String m_givenName;
  /** Surname name of the person */
  private String m_surname;
  /** Email of the person. */
  private String m_email;
  /** Set of teams the person is responsible for (i.e. owning them) */
  private Set<TeamEntity> m_responsibleFor;
  /** Set of teams the person is member of */
  private Set<TeamEntity> m_memberOf;
  /** Timestamp of the last synchronization */
  private Date m_lastSynchronize;

  protected PersonEntity()
  {
  }

  public PersonEntity(final String id, final long revision, final long lastModifiedByClient)
  {
    m_id = id;
    m_revision = revision;
    m_lastModifiedByClient = lastModifiedByClient;
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

  @Column(length = 50, nullable = true)
  public String getGivenName()
  {
    return m_givenName;
  }

  public void setGivenName(final String givenName)
  {
    m_givenName = givenName;
  }

  @Column(length = 50, nullable = false)
  public String getSurname()
  {
    return m_surname;
  }

  public void setSurname(final String surname)
  {
    m_surname = surname;
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

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
  public Set<TeamEntity> getMemberOf()
  {
    return m_memberOf;
  }

  public void setMemberOf(final Set<TeamEntity> memberOf)
  {
    m_memberOf = memberOf;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "owners")
  public Set<TeamEntity> getResponsibleFor()
  {
    return m_responsibleFor;
  }

  public void setResponsibleFor(final Set<TeamEntity> responsibleFor)
  {
    m_responsibleFor = responsibleFor;
  }

  @Column(nullable = true)
  public Date getLastSynchronize()
  {
    return m_lastSynchronize;
  }

  public void setLastSynchronize(final Date lastSynchronize)
  {
    m_lastSynchronize = lastSynchronize;
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

  @Override
  public int hashCode()
  {
    return m_id.hashCode();
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof PersonEntity)) {
      return false;
    }

    final PersonEntity castObj = (PersonEntity) obj;

    return m_id.equals(castObj.m_id);
  }

  public void toDTO(final Person person)
  {
    person.setId(m_id);
    person.setRevision(m_revision);
    person.setLastModifiedByClient(m_lastModifiedByClient);
    person.setGivenName(m_givenName);
    person.setSurname(m_surname);
    person.setEmail(m_email);
    person.setLastSynchronize(m_lastSynchronize);
  }

  public void fromDTO(final Person person)
  {
    m_lastModifiedByClient = person.getLastModifiedByClient();
    m_givenName = person.getGivenName();
    m_surname = person.getSurname();
    m_email = person.getEmail();
  }
}
