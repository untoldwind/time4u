package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
  /** Ids of teams that have interest in this todo. */
  private List<String> visibleToTeamIds;
  /** Ids of persons that have interest in this todo. */
  private List<String> visibleToPersonIds;
  /** Todo assignements. */
  private List<TodoAssignment> m_assignments;
  /** Map of all meta properties of the team. */
  private Map<String, MetaProperty> m_metaProperties;

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

  public List<String> getVisibleToTeamIds()
  {
    return visibleToTeamIds;
  }

  public void setVisibleToTeamIds(final List<String> visibleToTeamIds)
  {
    this.visibleToTeamIds = visibleToTeamIds;
  }

  public List<String> getVisibleToPersonIds()
  {
    return visibleToPersonIds;
  }

  public void setVisibleToPersonIds(final List<String> visibleToPersonIds)
  {
    this.visibleToPersonIds = visibleToPersonIds;
  }

  public List<TodoAssignment> getAssignments()
  {
    return m_assignments;
  }

  public void setAssignments(final List<TodoAssignment> assignments)
  {
    m_assignments = assignments;
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
