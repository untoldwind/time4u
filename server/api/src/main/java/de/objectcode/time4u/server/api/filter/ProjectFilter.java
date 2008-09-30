package de.objectcode.time4u.server.api.filter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * A filter condition for querying projects.
 * 
 * @author junglas
 */
@XmlType(name = "project-filter")
public class ProjectFilter implements Serializable
{
  private static final long serialVersionUID = 4840094656452995441L;

  /** Condition for the parent project (optional, 0L = root project). */
  String m_parentProject;
  /** Condition for the active flag (optional). */
  Boolean m_active;
  /** Condition for the delete flag (optional). */
  Boolean m_deleted;
  /** Minimum revision number (i.e. only revisions greater or equals are returned). */
  Long m_minRevision;
  /** Client id of the last modification */
  Long m_lastModifiedByClient;
  /** Desired order */
  Order m_order;

  public ProjectFilter()
  {
    m_order = Order.ID;
  }

  public ProjectFilter(final Boolean active, final Boolean deleted, final Long minRevision, final String parentProject,
      final Order order)
  {
    m_active = active;
    m_deleted = deleted;
    m_minRevision = minRevision;
    m_parentProject = parentProject;
    m_order = order;
  }

  public String getParentProject()
  {
    return m_parentProject;
  }

  public Boolean getActive()
  {
    return m_active;
  }

  public Boolean getDeleted()
  {
    return m_deleted;
  }

  public Long getMinRevision()
  {
    return m_minRevision;
  }

  public void setParentProject(final String parentProject)
  {
    m_parentProject = parentProject;
  }

  public void setActive(final Boolean active)
  {
    m_active = active;
  }

  public void setDeleted(final Boolean deleted)
  {
    m_deleted = deleted;
  }

  public void setMinRevision(final Long minRevision)
  {
    m_minRevision = minRevision;
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

  /**
   * Convenient method to create a "only top level projects" filter.
   * 
   * @param onlyActive
   *          <tt>true</tt> if only active projects should be filtered
   * @return The desired filter condition
   */
  public static ProjectFilter filterRootProjects(final boolean onlyActive)
  {
    return new ProjectFilter(onlyActive ? true : null, false, null, "", Order.NAME);
  }

  /**
   * Convenient method to create a "only sub-projects of a given project" filter.
   * 
   * @param parentProjectId
   *          The id of the parent project
   * @param onlyActive
   *          <tt>true</tt> if only active projects should be filtered
   * @return The desired filter condition
   */
  public static ProjectFilter filterChildProjects(final String parentProjectId, final boolean onlyActive)
  {
    return new ProjectFilter(onlyActive ? true : null, false, null, parentProjectId, Order.NAME);
  }

  public static enum Order
  {
    ID,
    NAME
  }
}
