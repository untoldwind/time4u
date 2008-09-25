package de.objectcode.time4u.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * User role entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_ROLES")
public class RoleEntity
{
  /** Primary key. */
  private long m_id;
  /** Role id. */
  private String m_roleId;
  /** Role name. */
  private String m_name;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_ROLES")
  @GenericGenerator(name = "SEQ_T4U_ROLES", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_ROLES"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @Column(length = 30, nullable = false, unique = true)
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
