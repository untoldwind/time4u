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
 * Task history entity. This is part of the audit information.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TASKS_HISTORY")
public class TaskHistoryEntity
{
  /** Primary key. */
  private long m_id;
  /** Task this history entry belongs to. */
  private TaskEntity m_task;
  /** Person who performed the change. */
  private PersonEntity m_performedBy;
  /** Timestamp of the change. */
  private Date m_performedAt;
  /** Original name of the task. */
  private String m_name;
  /** Original description of the task. */
  private String m_description;
  /** Original active flag. */
  private boolean m_active;
  /** Original deleted flag. */
  private boolean m_deleted;
  /** Original project of the task. */
  private ProjectEntity m_project;

  public TaskHistoryEntity()
  {
  }

  public TaskHistoryEntity(final TaskEntity task, final PersonEntity performedBy)
  {
    m_task = task;
    m_performedBy = performedBy;
    m_performedAt = new Date();

    m_name = task.getName();
    m_description = task.getDescription();
    m_project = task.getProject();
    m_active = task.isActive();
    m_deleted = task.isDeleted();
  }

  @GeneratedValue
  @Id
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  public boolean isActive()
  {
    return m_active;
  }

  public void setActive(final boolean active)
  {
    m_active = active;
  }

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  @Column(length = 30, nullable = true)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Column(length = 1000, nullable = true)
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @JoinColumn(name = "project_id", nullable = true)
  @ManyToOne
  public ProjectEntity getProject()
  {
    return m_project;
  }

  public void setProject(final ProjectEntity project)
  {
    m_project = project;
  }

  @JoinColumn(name = "task_id", nullable = false)
  @ManyToOne
  public TaskEntity getTask()
  {
    return m_task;
  }

  public void setTask(final TaskEntity task)
  {
    m_task = task;
  }

  @JoinColumn(name = "performedBy_id", nullable = false)
  @ManyToOne
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
