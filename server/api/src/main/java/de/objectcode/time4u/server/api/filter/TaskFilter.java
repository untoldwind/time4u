package de.objectcode.time4u.server.api.filter;

import java.io.Serializable;
import java.util.UUID;

/**
 * A filter condition for querying tasks.
 * 
 * @author junglas
 */
public class TaskFilter implements Serializable
{
  private static final long serialVersionUID = 6557945403193307426L;

  /** Condition for the project (optional). */
  UUID m_project;
  /** Condition for the active flag (optional). */
  Boolean m_active;
  /** Condition for the delete flag (optional). */
  Boolean m_deleted;
  /** Minimum revision number (i.e. only revisions greater or equals are returned). */
  Long m_minRevision;
  /** Desired order. */
  Order m_order;

  public TaskFilter()
  {
    m_order = Order.ID;
  }

  public TaskFilter(final Boolean active, final Boolean deleted, final Long minRevision, final UUID project,
      final Order order)
  {
    m_active = active;
    m_deleted = deleted;
    m_minRevision = minRevision;
    m_project = project;
    m_order = order;
  }

  public UUID getProject()
  {
    return m_project;
  }

  public void setProject(final UUID project)
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

  public Order getOrder()
  {
    return m_order;
  }

  public void setOrder(final Order order)
  {
    m_order = order;
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
  public static TaskFilter filterProjectTasks(final UUID projectId, final boolean onlyActive)
  {
    return new TaskFilter(onlyActive ? true : null, false, null, projectId, Order.NAME);
  }

  public static enum Order
  {
    ID,
    NAME;
  }
}
