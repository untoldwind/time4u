package de.objectcode.time4u.client.store.api;

/**
 * A filter condition for querying projects.
 * 
 * @author junglas
 */
public class ProjectFilter
{
  /** Condition for the parent project (optional, 0L = root project). */
  final Long m_parentProject;
  /** Condition for the active flag (optional). */
  final Boolean m_active;
  /** Condition for the delete flag (optional). */
  final Boolean m_deleted;
  /** Minimum revision number (i.e. only revisions greater or equals are returned). */
  final Long m_minRevision;

  public ProjectFilter(final Boolean active, final Boolean deleted, final Long minRevision, final Long parentProject)
  {
    m_active = active;
    m_deleted = deleted;
    m_minRevision = minRevision;
    m_parentProject = parentProject;
  }

  public Long getParentProject()
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

}
