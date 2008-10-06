package de.objectcode.time4u.server.entities.account;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.objectcode.time4u.server.entities.PersonEntity;

@Entity
@Table(name = "T4U_USERACCOUNTS")
public class UserAccountEntity
{
  /** User id (primary key) */
  private String m_userId;
  /** Encoded password of the person. */
  private String m_hashedPassword;
  /** Last login for this account */
  private Date m_lastLogin;
  /** Identity */
  PersonEntity m_person;
  /** User roles. */
  private Set<UserRoleEntity> m_roles;

  /**
   * Default constructor for hibernate.
   */
  protected UserAccountEntity()
  {
  }

  public UserAccountEntity(final String userId, final String hashedPassword, final PersonEntity person)
  {
    m_userId = userId;
    m_hashedPassword = hashedPassword;
    m_person = person;
    m_roles = new HashSet<UserRoleEntity>();
  }

  @Id
  @Column(length = 30)
  public String getUserId()
  {
    return m_userId;
  }

  public void setUserId(final String userId)
  {
    m_userId = userId;
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

  public Date getLastLogin()
  {
    return m_lastLogin;
  }

  public void setLastLogin(final Date lastLogin)
  {
    m_lastLogin = lastLogin;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id")
  public PersonEntity getPerson()
  {
    return m_person;
  }

  public void setPerson(final PersonEntity person)
  {
    m_person = person;
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "T4U_USERACCOUNT_USERROLES", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = { @JoinColumn(name = "roleId") })
  public Set<UserRoleEntity> getRoles()
  {
    return m_roles;
  }

  public void setRoles(final Set<UserRoleEntity> roles)
  {
    m_roles = roles;
  }
}
