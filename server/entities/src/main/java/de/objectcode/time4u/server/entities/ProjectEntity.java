package de.objectcode.time4u.server.entities;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.MetaType;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.entities.context.IPersistenceContext;

/**
 * Project entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_PROJECTS")
public class ProjectEntity
{
  /** Primary key */
  private String m_id;
  /** Project name */
  private String m_name;
  /** Project description */
  private String m_description;
  /** Flag if the project is active */
  private boolean m_active;
  /** Flag if the project is deleted (it might be still referenced somewhere) */
  private boolean m_deleted;
  /** Parent project */
  private ProjectEntity m_parent;
  /** Meta properties of the project */
  private Map<String, ProjectProperty> m_metaProperties;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Helper string containing all primary keys of all parent projects (usefull for querying all sub-projects. */
  private String m_parentKey;

  /**
   * Default constructor for hibernate.
   */
  protected ProjectEntity()
  {
  }

  public ProjectEntity(final String id, final long revsion, final long lastModifiedByClient, final String name)
  {
    m_id = id;
    m_revision = revsion;
    m_lastModifiedByClient = lastModifiedByClient;
    m_name = name;
  }

  @Id
  @Column(length = 36)
  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
  {
    m_id = id;
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

  @Column(length = 40, nullable = false)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Column(length = 1000, nullable = true)
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @ManyToOne(optional = true)
  @JoinColumn(name = "parent_id")
  public ProjectEntity getParent()
  {
    return m_parent;
  }

  public void setParent(final ProjectEntity parent)
  {
    m_parent = parent;
  }

  @MapKey(name = "name")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
  public Map<String, ProjectProperty> getMetaProperties()
  {
    return m_metaProperties;
  }

  public void setMetaProperties(final Map<String, ProjectProperty> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  public boolean inheritsFrom(final ProjectEntity masterProject)
  {
    if (m_parent != null) {
      if (m_parent.getId() == masterProject.getId()) {
        return true;
      } else {
        return m_parent.inheritsFrom(masterProject);
      }
    }

    return false;
  }

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  @Column(name = "parentKey", length = 740)
  public String getParentKey()
  {
    return m_parentKey;
  }

  public void setParentKey(final String parentKey)
  {
    m_parentKey = parentKey;
  }

  public void updateParentKey()
  {
    if (m_parent == null) {
      m_parentKey = m_id;
    } else {
      if (m_parent.getParentKey() == null) {
        m_parent.updateParentKey();
      }
      m_parentKey = m_parent.getParentKey() + "." + m_id;
    }
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof ProjectEntity)) {
      return false;
    }

    final ProjectEntity castObj = (ProjectEntity) obj;

    return m_id == castObj.m_id;
  }

  public void toSummaryDTO(final ProjectSummary project)
  {
    project.setId(m_id);
    project.setRevision(m_revision);
    project.setLastModifiedByClient(m_lastModifiedByClient);
    project.setActive(m_active);
    project.setDeleted(m_deleted);
    project.setName(m_name);
    project.setParentId(m_parent != null ? m_parent.getId() : null);
  }

  public void toDTO(final Project project)
  {
    toSummaryDTO(project);
    project.setDescription(m_description);

    if (m_metaProperties != null) {
      for (final ProjectProperty property : m_metaProperties.values()) {
        if (property.getBoolValue() != null) {
          project.setMetaProperty(new MetaProperty(property.getName(), property.getBoolValue()));
        } else if (property.getStrValue() != null) {
          project.setMetaProperty(new MetaProperty(property.getName(), property.getStrValue()));
        } else if (property.getDateValue() != null) {
          project.setMetaProperty(new MetaProperty(property.getName(), property.getDateValue()));
        } else if (property.getIntValue() != null) {
          project.setMetaProperty(new MetaProperty(property.getName(), property.getIntValue()));
        }
      }
    }
  }

  public void fromDTO(final IPersistenceContext context, final Project project)
  {
    m_lastModifiedByClient = project.getLastModifiedByClient();
    m_active = project.isActive();
    m_deleted = project.isDeleted();
    m_name = project.getName() != null ? project.getName() : "";
    m_parent = project.getParentId() != null ? context.findProject(project.getParentId(), project
        .getLastModifiedByClient()) : null;
    updateParentKey();
    m_description = project.getDescription();

    if (m_metaProperties == null) {
      m_metaProperties = new HashMap<String, ProjectProperty>();
    }

    if (project.getMetaProperties() != null) {
      for (final MetaProperty property : project.getMetaProperties().values()) {
        ProjectProperty projectProperty = m_metaProperties.get(property.getName());

        if (projectProperty == null) {
          projectProperty = new ProjectProperty();
          projectProperty.setName(property.getName());
          projectProperty.setProject(this);

          m_metaProperties.put(property.getName(), projectProperty);
        }

        switch (MetaType.valueOf(property.getType())) {
          case STRING: {
            projectProperty.setStrValue(property.getValue());
            break;
          }

          case INTEGER: {
            projectProperty.setIntValue(Integer.parseInt(property.getValue()));
            break;
          }

          case BOOLEAN: {
            projectProperty.setBoolValue(Boolean.parseBoolean(property.getValue()));
            break;
          }

          case DATE: {
            try {
              projectProperty.setDateValue(MetaProperty.g_format.parse(property.getValue()));
            } catch (final ParseException e) {
            }
            break;
          }
        }
      }
    }

  }
}
