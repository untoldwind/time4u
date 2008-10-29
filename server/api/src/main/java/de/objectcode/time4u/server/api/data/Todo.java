package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Todo DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "todo")
@XmlRootElement(name = "todo")
public class Todo extends TodoSummary
{
  private static final long serialVersionUID = 5927951499996904471L;

  /** The server id of the task the todo belongs to. */
  private String m_taskId;
  /** Priority of the todo. */
  private int m_priority;
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
