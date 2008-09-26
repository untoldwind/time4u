package de.objectcode.time4u.server.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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
  private long m_id;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** User id of the person. */
  private String m_userId;
  /** Encoded password of the person. */
  private String m_hashedPassword;
  /** Real name of the person. */
  private String m_name;
  /** Email of the person. */
  private String m_email;
  /** User roles. */
  private Set<RoleEntity> m_roles;
  /** Set of teams the person is responsible for (i.e. owning them) */
  private Set<TeamEntity> m_responsibleFor;
  /** Set of teams the person is member of */
  private Set<TeamEntity> m_memberOf;
  /** Timestamp of the last synchronization */
  private Date m_lastSynchronize;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_PERSONS")
  @GenericGenerator(name = "SEQ_T4U_PERSONS", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_PERSONS"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @Column(length = 100, nullable = true)
  public String getHashedPassword()
  {
    return m_hashedPassword;
  }

  public void setHashedPassword(final String hashedPassword)
  {
    m_hashedPassword = hashedPassword;
  }

  @Column(length = 30, nullable = false, unique = true)
  public String getUserId()
  {
    return m_userId;
  }

  public void setUserId(final String userId)
  {
    m_userId = userId;
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

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "T4U_PERSONS_ROLES", joinColumns = { @JoinColumn(name = "person_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
  public Set<RoleEntity> getRoles()
  {
    return m_roles;
  }

  public void setRoles(final Set<RoleEntity> roles)
  {
    m_roles = roles;
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

  @Override
  public int hashCode()
  {
    return (int) (m_id ^ m_id >>> 32);
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

    return m_id == castObj.m_id;
  }

  public void toDTO(final Person person)
  {
    person.setId(m_id);
    person.setRevision(m_revision);
    person.setName(m_name);
    person.setUserId(m_userId);
    person.setEmail(m_email);
    person.setLastSynchronize(m_lastSynchronize);
  }

  public void fromDTO(final Person person)
  {
    m_name = person.getName();
    m_email = person.getEmail();
  }
}
