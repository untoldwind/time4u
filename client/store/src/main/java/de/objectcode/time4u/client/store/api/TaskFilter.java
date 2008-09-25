package de.objectcode.time4u.client.store.api;

/**
 * A filter condition for querying tasks.
 * 
 * @author junglas
 */
public class TaskFilter
{
  /** Condition for the project (optional). */
  Long m_project;
  /** Condition for the active flag (optional). */
  Boolean m_active;
  /** Condition for the delete flag (optional). */
  Boolean m_deleted;
  /** Minimum revision number (i.e. only revisions greater or equals are returned). */
  Long m_minRevision;

  public TaskFilter()
  {
  }

  public TaskFilter(final Boolean active, final Boolean deleted, final Long minRevision, final Long project)
  {
    m_active = active;
    m_deleted = deleted;
    m_minRevision = minRevision;
    m_project = project;
  }

  public Long getProject()
  {
    return m_project;
  }

  public void setProject(final Long project)
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

}
