package de.objectcode.time4u.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T4U_DAYTAGS")
public class DayTagEntity
{
  /** Primary key */
  private String m_name;
  /** Tag description */
  private String m_description;

  @Id
  @Column(length = 50)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Column(length = 200, nullable = false)
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

}
