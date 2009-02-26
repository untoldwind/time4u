package de.objectcode.time4u.server.api.filter;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.TodoState;

/**
 * Filter condition for todo queries.
 * 
 * @author junglas
 */
@XmlType(name = "todo-filter")
public class TodoFilter implements Serializable
{
  private static final long serialVersionUID = -360641282458569560L;

  /** Condition for the delete flag (optional). */
  Boolean m_deleted;
  /** Minimum (inclusive) revision number (i.e. only revisions greater or equals are returned). */
  Long m_minRevision;
  /** Maximum (inclusive) revision number (i.e. only revisions less or equals are returned). */
  Long m_maxRevision;
  /** Client id of the last modification */
  Long m_lastModifiedByClient;
  /** Group id (optional, "" = todo without group). */
  String m_groupId;
  /** Filter todo groups */
  Boolean m_group;
  /** Filter todos associated with a certain task */
  String m_taskId;
  /** An array of desired todo states */
  TodoState[] m_todoStates;
  /** Optional assignement filter */
  AssignmentFilter m_assignmentFilter;
  /** CompletedAt is greater or equals this value. */
  Date m_completedAtGe;
  /** CreatedAt is greater or equals this value. */
  Date m_createdAtGe;
  /** Desired order */
  Order m_order;

  public TodoFilter()
  {
    m_order = Order.ID;
  }

  public TodoFilter(final Boolean deleted, final Long minRevision, final Long maxRevision, final String groupId,
      final Order order)
  {
    m_deleted = deleted;
    m_minRevision = minRevision;
    m_maxRevision = maxRevision;
    m_groupId = groupId;
    m_order = order;
  }

  public Boolean getDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final Boolean deleted)
  {
    m_deleted = deleted;
  }

  public Long getMinRevision()
  {
    return m_minRevision;
  }

  public void setMinRevision(final Long minRevision)
  {
    m_minRevision = minRevision;
  }

  public Long getMaxRevision()
  {
    return m_maxRevision;
  }

  public void setMaxRevision(final Long maxRevision)
  {
    m_maxRevision = maxRevision;
  }

  public Long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final Long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public String getGroupId()
  {
    return m_groupId;
  }

  public void setGroupId(final String groupId)
  {
    m_groupId = groupId;
  }

  public Boolean getGroup()
  {
    return m_group;
  }

  public void setGroup(final Boolean group)
  {
    m_group = group;
  }

  public String getTaskId()
  {
    return m_taskId;
  }

  public void setTaskId(final String taskId)
  {
    m_taskId = taskId;
  }

  public TodoState[] getTodoStates()
  {
    return m_todoStates;
  }

  public void setTodoStates(final TodoState[] todoStates)
  {
    m_todoStates = todoStates;
  }

  public AssignmentFilter getAssignmentFilter()
  {
    return m_assignmentFilter;
  }

  public void setAssignmentFilter(final AssignmentFilter assignmentFilter)
  {
    m_assignmentFilter = assignmentFilter;
  }

  public Date getCompletedAtGe()
  {
    return m_completedAtGe;
  }

  public void setCompletedAtGe(final Date completedAtGe)
  {
    m_completedAtGe = completedAtGe;
  }

  public Date getCreatedAtGe()
  {
    return m_createdAtGe;
  }

  public void setCreatedAtGe(final Date createdAtGe)
  {
    m_createdAtGe = createdAtGe;
  }

  public Order getOrder()
  {
    return m_order;
  }

  public void setOrder(final Order order)
  {
    m_order = order;
  }

  public static TodoFilter filterRootTodos()
  {
    return new TodoFilter(false, null, null, "", Order.HEADER);
  }

  public static TodoFilter filterTodos(final String groupId)
  {
    return new TodoFilter(false, null, null, groupId, Order.HEADER);
  }

  public static TodoFilter filterRootTodoGroups()
  {
    final TodoFilter filter = new TodoFilter(false, null, null, "", Order.HEADER);

    filter.setGroup(true);

    return filter;
  }

  public static TodoFilter filterTodoGroups(final String groupId)
  {
    final TodoFilter filter = new TodoFilter(false, null, null, groupId, Order.HEADER);

    filter.setGroup(true);

    return filter;
  }

  public static TodoFilter filterTodoForTask(final String taskId)
  {
    final TodoFilter filter = new TodoFilter(false, null, null, null, Order.HEADER);

    filter.setTaskId(taskId);
    filter.setGroup(false);

    return filter;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("TodoFilter(");
    buffer.append("deleted=").append(m_deleted);
    buffer.append(",minRevision=").append(m_minRevision);
    buffer.append(",maxRevision=").append(m_maxRevision);
    buffer.append(",lastModifiedByClient=").append(m_lastModifiedByClient);
    buffer.append(",groupId=").append(m_groupId);
    buffer.append(",group=").append(m_group);
    buffer.append(",taskId=").append(m_taskId);
    buffer.append(",order=").append(m_order);
    buffer.append(")");

    return buffer.toString();
  }

  public static enum Order
  {
    ID,
    HEADER
  }

  public static class AssignmentFilter
  {
    boolean m_unassigned;
    boolean m_assignedToPerson;
    boolean m_assignedToOther;
    String m_personId;

    public AssignmentFilter(final boolean unassigned, final boolean assignedToPerson, final boolean assignedToOther,
        final String personId)
    {
      m_unassigned = unassigned;
      m_assignedToPerson = assignedToPerson;
      m_assignedToOther = assignedToOther;
      m_personId = personId;
    }

    public boolean isUnassigned()
    {
      return m_unassigned;
    }

    public boolean isAssignedToPerson()
    {
      return m_assignedToPerson;
    }

    public boolean isAssignedToOther()
    {
      return m_assignedToOther;
    }

    public String getPersonId()
    {
      return m_personId;
    }

    public void setUnassigned(final boolean unassigned)
    {
      m_unassigned = unassigned;
    }

    public void setAssignedToPerson(final boolean assignedToPerson)
    {
      m_assignedToPerson = assignedToPerson;
    }

    public void setAssignedToOther(final boolean assignedToOther)
    {
      m_assignedToOther = assignedToOther;
    }

    public void setPersonId(final String personId)
    {
      m_personId = personId;
    }
  }
}
