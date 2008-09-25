package de.objectcode.time4u.server.api.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Project DTO object.
 * 
 * @author junglas
 */
public class Project implements ISynchronizableData
{
  private static final long serialVersionUID = 7628044489809504348L;

  /** The internal server id of the project */
  private long m_id;
  /** Project name. */
  private String m_name;
  /** Project description. */
  private String m_description;
  /** Internal id of the parent project (<tt>null</tt> for root level projects) */
  private Long m_parentId;
  /** Flag if the project is active. */
  private boolean m_active;
  /** Flag if the project is deleted. */
  private boolean m_deleted;
  /** Flag if the project has sub-projects. */
  private boolean m_hasSubProjects;
  /** Flag if the project has tasks. */
  private boolean m_hasTasks;
  /** Map of all meta properties of the project. */
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
   * @return the parentId
   */
  public Long getParentId()
  {
    return m_parentId;
  }

  public boolean isHasSubProjects()
  {
    return m_hasSubProjects;
  }

  public void setHasSubProjects(final boolean hasSubProjects)
  {
    m_hasSubProjects = hasSubProjects;
  }

  public boolean isHasTasks()
  {
    return m_hasTasks;
  }

  public void setHasTasks(final boolean hasTasks)
  {
    m_hasTasks = hasTasks;
  }

  /**
   * @param parentId
   *          the parentId to set
   */
  public void setParentId(final Long parentId)
  {
    m_parentId = parentId;
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

    if (obj == null || !(obj instanceof Project)) {
      return false;
    }

    final Project castObj = (Project) obj;

    return m_id == castObj.m_id;
  }
}
