package de.objectcode.time4u.server.api.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Task DTO object.
 * 
 * @author junglas
 */
public class Task implements ISynchronizableData
{
  private static final long serialVersionUID = -6761887763470951635L;

  /** Internal server id of the task. */
  private long m_id;
  /** Revision number. */
  private long m_revision;
  /** Internal server id of the project owning the task. */
  private Long m_projectId;
  /** Task name. */
  private String m_name;
  /** Task description. */
  private String m_description;
  /** Flag if the task is active. */
  private boolean m_active;
  /** Flag if the task is deleted. */
  private boolean m_deleted;
  /** Map of a meta properties. */
  private List<MetaProperty> m_metaProperties;

  /**
   * @return the active
   */
  public boolean isActive()
  {
    return m_active;
  }

  /**
   * @param active
   *          the active to set
   */
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
   * @return the id
   */
  public long getId()
  {
    return m_id;
  }

  /**
   * @param id
   *          the id to set
   */
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

  /**
   * @return the name
   */
  public String getName()
  {
    return m_name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(final String name)
  {
    m_name = name;
  }

  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  /**
   * @return the projectId
   */
  public Long getProjectId()
  {
    return m_projectId;
  }

  /**
   * @param projectId
   *          the projectId to set
   */
  public void setProjectId(final Long projectId)
  {
    m_projectId = projectId;
  }

  public List<MetaProperty> getMetaProperties()
  {
    return m_metaProperties;
  }

  public void setMetaProperties(final List<MetaProperty> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  public void addMetaProperties(final MetaProperty metaProperty)
  {
    if (m_metaProperties == null) {
      m_metaProperties = new ArrayList<MetaProperty>();
    }
    m_metaProperties.add(metaProperty);
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof Task)) {
      return false;
    }

    final Task castObj = (Task) obj;

    return m_id == castObj.m_id;
  }

}
