package de.objectcode.time4u.server.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Task meta property entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TASK_PROPERTIES")
public class TaskProperty
{
  /** Primary key */
  private long m_id;
  /** Name of the property */
  private String m_name;
  /** String value (if it is a string) */
  private String m_strValue;
  /** Integer value (if it is an integer) */
  private Integer m_intValue;
  /** Boolean value (if it is a boolean) */
  private Boolean m_boolValue;
  /** Date/timestamp value (if it is a date/timestamp) */
  private Date m_dateValue;
  /** The task this meta property belongs to. */
  private TaskEntity m_task;

  @Id
  @GeneratedValue
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @Column(length = 200, nullable = false)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @ManyToOne
  @JoinColumn(name = "task_id", nullable = false)
  public TaskEntity getTask()
  {
    return m_task;
  }

  public void setTask(final TaskEntity task)
  {
    m_task = task;
  }

  @Column(name = "strValue", length = 1000, nullable = true)
  public String getStrValue()
  {
    return m_strValue;
  }

  public void setStrValue(final String strValue)
  {
    m_strValue = strValue;
  }

  @Column(name = "boolValue", nullable = true)
  public Boolean getBoolValue()
  {
    return m_boolValue;
  }

  public void setBoolValue(final Boolean boolValue)
  {
    m_boolValue = boolValue;
  }

  @Column(name = "dateValue", nullable = true)
  public Date getDateValue()
  {
    return m_dateValue;
  }

  public void setDateValue(final Date dateValue)
  {
    m_dateValue = dateValue;
  }

  @Column(name = "intValue", nullable = true)
  public Integer getIntValue()
  {
    return m_intValue;
  }

  public void setIntValue(final Integer intValue)
  {
    m_intValue = intValue;
  }

}
