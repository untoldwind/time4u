package de.objectcode.time4u.server.api.data;

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
  private long m_id;
  /** Revision number. */
  private long m_revision;
  /** Task name. */
  private String m_name;
  /** Flag if the task is active. */
  private boolean m_active;
  /** Flag if the task is deleted. */
  private boolean m_deleted;

  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
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
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return (int) (m_id ^ m_id >>> 32);
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

    final TaskSummary castObj = (Task) obj;

    return m_id == castObj.m_id;
  }

}
