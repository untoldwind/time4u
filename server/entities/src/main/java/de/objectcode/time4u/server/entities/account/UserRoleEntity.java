package de.objectcode.time4u.server.entities.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User role entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_USERROLES")
public class UserRoleEntity
{
  /** Role id. */
  private String m_roleId;
  /** Role name. */
  private String m_name;

  /**
   * Default constructor for hibernate.
   */
  protected UserRoleEntity()
  {
  }

  public UserRoleEntity(final String roleId, final String name)
  {
    m_roleId = roleId;
    m_name = name;
  }

  @Id
  @Column(length = 30)
  public String getRoleId()
  {
    return m_roleId;
  }

  public void setRoleId(final String roleId)
  {
    m_roleId = roleId;
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

}
