package de.objectcode.time4u.server.api.filter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * A filter condition for querying tasks.
 * 
 * @author junglas
 */
@XmlType(name = "task-filter")
public class TaskFilter implements Serializable
{
  private static final long serialVersionUID = 6557945403193307426L;

  /** Condition for the project (optional). */
  String m_project;
  /** Condition for the active flag (optional). */
  Boolean m_active;
  /** Condition for the delete flag (optional). */
  Boolean m_deleted;
  /** Minimum (inclusive) revision number (i.e. only revisions greater or equals are returned). */
  Long m_minRevision;
  /** Maximum (inclusive) revision number (i.e. only revisions less or equals are returned). */
  Long m_maxRevision;
  /** Client id of the last modification */
  Long m_lastModifiedByClient;
  /** Desired order. */
  Order m_order;

  public TaskFilter()
  {
    m_order = Order.ID;
  }

  public TaskFilter(final Boolean active, final Boolean deleted, final Long minRevision, final String project,
      final Order order)
  {
    m_active = active;
    m_deleted = deleted;
    m_minRevision = minRevision;
    m_project = project;
    m_order = order;
  }

  public String getProject()
  {
    return m_project;
  }

  public void setProject(final String project)
  {
    m_project = project;
  }

  public Boolean getActive()
  {
    return m_active;
  }

  public void setActive(final Boolean active)
  {
    m_active = active;
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

  public Order getOrder()
  {
    return m_order;
  }

  public void setOrder(final Order order)
  {
    m_order = order;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("TaskFilter(");
    buffer.append("active=").append(m_active);
    buffer.append(",deleted=").append(m_deleted);
    buffer.append(",project=").append(m_project);
    buffer.append(",minRevision=").append(m_minRevision);
    buffer.append(",maxRevision=").append(m_maxRevision);
    buffer.append(",lastModifiedByClient=").append(m_lastModifiedByClient);
    buffer.append(",order=").append(m_order);
    buffer.append(")");

    return buffer.toString();
  }

  /**
   * Convenient method to create a "only tasks of a given project" filter.
   * 
   * @param projectId
   *          The id of the project
   * @param onlyActive
   *          <tt>true</tt> if only active tasks should be filters
   * @return The desired filter condition
   */
  public static TaskFilter filterProjectTasks(final String projectId, final boolean onlyActive)
  {
    return new TaskFilter(onlyActive ? true : null, false, null, projectId, Order.NAME);
  }

  public static enum Order
  {
    ID,
    NAME;
  }
}
