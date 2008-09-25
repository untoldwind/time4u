package de.objectcode.time4u.server.api.data;

/**
 * Project DTO object.
 * 
 * This DTO only transfers the summary of a project.
 * 
 * @author junglas
 */
public class ProjectSummary implements ISynchronizableData
{
  private static final long serialVersionUID = 7578389791349119895L;

  /** The internal server id of the project */
  private long m_id;
  /** Revision number. */
  private long m_revision;
  /** Project name. */
  private String m_name;
  /** Flag if the project is active. */
  private boolean m_active;
  /** Flag if the project is deleted. */
  private boolean m_deleted;
  /** Internal id of the parent project (<tt>null</tt> for root level projects) */
  private Long m_parentId;

  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
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

  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  public boolean isActive()
  {
    return m_active;
  }

  public void setActive(final boolean active)
  {
    m_active = active;
  }

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  /**
   * @return the parentId
   */
  public Long getParentId()
  {
    return m_parentId;
  }

  /**
   * @param parentId
   *          the parentId to set
   */
  public void setParentId(final Long parentId)
  {
    m_parentId = parentId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return (int) (m_id ^ m_id >>> 32);
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

    if (obj == null || !(obj instanceof ProjectSummary)) {
      return false;
    }

    final ProjectSummary castObj = (Project) obj;

    return m_id == castObj.m_id;
  }

}
