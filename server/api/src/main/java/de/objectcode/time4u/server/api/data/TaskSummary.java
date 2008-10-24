package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Task DTO object.
 * 
 * This DTO only transfers the summary of a task.
 * 
 * @author junglas
 */
@XmlType(name = "task-summary")
@XmlRootElement(name = "task-summary")
public class TaskSummary implements ISynchronizableData
{
  private static final long serialVersionUID = -3947869946239621847L;

  /** Internal server id of the task. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Task name. */
  private String m_name;
  /** Flag if the task is active. */
  private boolean m_active;
  /** Flag if the task is deleted. */
  private boolean m_deleted;
  /** Internal server id of the project owning the task. */
  private String m_projectId;

  @XmlAttribute
  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
  {
    m_id = id;
  }

  @XmlAttribute
  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  @XmlAttribute
  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  @XmlAttribute
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @XmlAttribute
  public boolean isActive()
  {
    return m_active;
  }

  public void setActive(final boolean active)
  {
    m_active = active;
  }

  @XmlAttribute
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
  @XmlAttribute
  public String getProjectId()
  {
    return m_projectId;
  }

  /**
   * @param projectId
   *          the projectId to set
   */
  public void setProjectId(final String projectId)
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
