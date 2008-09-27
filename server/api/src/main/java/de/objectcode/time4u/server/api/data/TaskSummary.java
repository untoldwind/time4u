package de.objectcode.time4u.server.api.data;

import java.util.UUID;

/**
 * Task DTO object.
 * 
 * This DTO only transfers the summary of a task.
 * 
 * @author junglas
 */
public class TaskSummary implements ISynchronizableData
{
  private static final long serialVersionUID = -3947869946239621847L;

  /** Internal server id of the task. */
  private UUID m_id;
  /** Revision number. */
  private long m_revision;
  /** Task name. */
  private String m_name;
  /** Flag if the task is active. */
  private boolean m_active;
  /** Flag if the task is deleted. */
  private boolean m_deleted;
  /** Internal server id of the project owning the task. */
  private UUID m_projectId;

  public UUID getId()
  {
    return m_id;
  }

  public void setId(final UUID id)
  {
    m_id = id;
  }

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
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

  /**
   * @return the projectId
   */
  public UUID getProjectId()
  {
    return m_projectId;
  }

  /**
   * @param projectId
   *          the projectId to set
   */
  public void setProjectId(final UUID projectId)
  {
    m_projectId = projectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return m_id != null ? m_id.hashCode() : 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof TaskSummary)) {
      return false;
    }

    final TaskSummary castObj = (TaskSummary) obj;

    if (m_id == null) {
      return false;
    }
    return m_id.equals(castObj.m_id);
  }

}
