package de.objectcode.time4u.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.DayTag;

@Entity
@Table(name = "T4U_DAYTAGS")
public class DayTagEntity
{
  /** Primary key */
  private String m_name;
  /** Tag description */
  private String m_description;
  /** Regular time of the day (only set this if the regular time of that day should be modified) */
  private Integer m_regularTime;

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

  public Integer getRegularTime()
  {
    return m_regularTime;
  }

  public void setRegularTime(final Integer regularTime)
  {
    m_regularTime = regularTime;
  }

  public void toDTO(final DayTag dayTag)
  {
    dayTag.setName(m_name);
    dayTag.setDescription(m_description);
    dayTag.setRegularTime(m_regularTime);
  }

  public void fromDTO(final DayTag dayTag)
  {
    m_name = dayTag.getName();
    m_description = dayTag.getDescription();
    m_regularTime = dayTag.getRegularTime();
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof DayTagEntity)) {
      return false;
    }

    final DayTagEntity castObj = (DayTagEntity) obj;

    return m_name.equals(castObj.m_name);
  }

  @Override
  public int hashCode()
  {
    return m_name.hashCode();
  }

}
