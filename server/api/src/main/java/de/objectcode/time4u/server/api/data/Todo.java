package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

/**
 * Todo DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "todo")
public class Todo implements ISynchronizableData
{
  private static final long serialVersionUID = 5927951499996904471L;

  /** Internal server id of the todo. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Flag if the todo is deleted */
  private boolean m_deleted;
  /** The server id of the task the todo belongs to. */
  private String m_taskId;
  /** Optional server id of the person the todo is assigned to. */
  private String m_assignedToPersonId;
  /** Optional server id of the team the todo is assigned to. */
  private String m_assignedToTeamId;
  /** Optional server id of the person who created the todo. */
  private String m_reporterId;
  /** Priority of the todo. */
  private int m_priority;
  /** Todo header/title. */
  private String m_header;
  /** Todo description. */
  private String m_description;
  /** Flag if the todo has been completed. */
  private boolean m_completed;
  /** Timestamp the todo was created. */
  private Date m_createdAt;
  /** Optional timestamp the todo was completed. */
  private Date m_completedAt;
  /** Optional deadline of the todo. */
  private Date m_deadline;
  /** Map of all meta properties of the team. */
  private Map<String, MetaProperty> m_metaProperties;

  public String getReporterId()
  {
    return m_reporterId;
  }

  public void setReporterId(final String reporterId)
  {
    m_reporterId = reporterId;
  }

  public String getAssignedToPersonId()
  {
    return m_assignedToPersonId;
  }

  public void setAssignedToPersonId(final String assignedToPersonId)
  {
    m_assignedToPersonId = assignedToPersonId;
  }

  public String getAssignedToTeamId()
  {
    return m_assignedToTeamId;
  }

  public void setAssignedToTeamId(final String assignedToTeamId)
  {
    m_assignedToTeamId = assignedToTeamId;
  }

  public boolean isCompleted()
  {
    return m_completed;
  }

  public void setCompleted(final boolean completed)
  {
    m_completed = completed;
  }

  public Date getCreatedAt()
  {
    return m_createdAt;
  }

  public void setCreatedAt(final Date createdAt)
  {
    m_createdAt = createdAt;
  }

  public Date getCompletedAt()
  {
    return m_completedAt;
  }

  public void setCompletedAt(final Date completedAt)
  {
    m_completedAt = completedAt;
  }

  public Date getDeadline()
  {
    return m_deadline;
  }

  public void setDeadline(final Date deadline)
  {
    m_deadline = deadline;
  }

  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  public String getHeader()
  {
    return m_header;
  }

  public void setHeader(final String header)
  {
    m_header = header;
  }

  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
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

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public int getPriority()
  {
    return m_priority;
  }

  public void setPriority(final int priority)
  {
    m_priority = priority;
  }

  public String getTaskId()
  {
    return m_taskId;
  }

  public void setTaskId(final String taskId)
  {
    m_taskId = taskId;
  }

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  public Map<String, MetaProperty> getMetaProperties()
  {
    if (m_metaProperties == null) {
      return Collections.emptyMap();
    }
    return m_metaProperties;
  }

  public void setMetaProperties(final Map<String, MetaProperty> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  public void setMetaProperty(final MetaProperty metaProperty)
  {
    if (m_metaProperties == null) {
      m_metaProperties = new HashMap<String, MetaProperty>();
    }
    m_metaProperties.put(metaProperty.getName(), metaProperty);
  }

  public MetaProperty getMetaProperty(final String name)
  {
    if (m_metaProperties != null) {
      return m_metaProperties.get(name);
    }
    return null;
  }

}
