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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.MetaType;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.entities.context.IPersistenceContext;

/**
 * Task entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TASKS")
public class TaskEntity
{
  /** Primary key. */
  private EntityKey m_id;
  /** Task name. */
  private String m_name;
  /** Task description. */
  private String m_description;
  /** Flag if the task is active. */
  private boolean m_active;
  /** Flag if the task is deleted. */
  private boolean m_deleted;
  /** The project the task belongs too. */
  private ProjectEntity m_project;
  /** All meta properties of the task. */
  private Map<String, TaskProperty> m_metaProperties;
  /** Revision number (increased every time something has changed) */
  private long m_revision;

  /**
   * Default constructor for hibernate.
   */
  protected TaskEntity()
  {
  }

  public TaskEntity(final EntityKey id, final long revision, final ProjectEntity project, final String name)
  {
    m_id = id;
    m_revision = revision;
    m_project = project;
    m_name = name;
  }

  @Id
  public EntityKey getId()
  {
    return m_id;
  }

  public void setId(final EntityKey id)
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

  @Column(length = 30, nullable = false)
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

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "project_clientId"), @JoinColumn(name = "project_localId") })
  public ProjectEntity getProject()
  {
    return m_project;
  }

  public void setProject(final ProjectEntity project)
  {
    m_project = project;
  }

  @MapKey(name = "name")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "task")
  public Map<String, TaskProperty> getMetaProperties()
  {
    return m_metaProperties;
  }

  public void setMetaProperties(final Map<String, TaskProperty> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof TaskEntity)) {
      return false;
    }

    final TaskEntity castObj = (TaskEntity) obj;

    return m_id == castObj.m_id;
  }

  public void toSummaryDTO(final TaskSummary task)
  {
    task.setId(m_id.getUUID());
    task.setRevision(m_revision);
    task.setActive(m_active);
    task.setDeleted(m_deleted);
    task.setName(m_name);
    task.setProjectId(m_project != null ? m_project.getId().getUUID() : null);
  }

  public void toDTO(final Task task)
  {
    toSummaryDTO(task);
    task.setDescription(m_description);

    if (m_metaProperties != null) {
      for (final TaskProperty property : m_metaProperties.values()) {
        if (property.getBoolValue() != null) {
          task.setMetaProperty(new MetaProperty(property.getName(), property.getBoolValue()));
        } else if (property.getStrValue() != null) {
          task.setMetaProperty(new MetaProperty(property.getName(), property.getStrValue()));
        } else if (property.getDateValue() != null) {
          task.setMetaProperty(new MetaProperty(property.getName(), property.getDateValue()));
        } else if (property.getIntValue() != null) {
          task.setMetaProperty(new MetaProperty(property.getName(), property.getIntValue()));
        }
      }
    }
  }

  public void fromDTO(final IPersistenceContext context, final Task task)
  {
    m_active = task.isActive();
    m_deleted = task.isDeleted();
    m_name = task.getName() != null ? task.getName() : "";
    if (task.getProjectId() != null) {
      m_project = context.findProject(task.getProjectId());
    } else {
      m_project = null;
    }
    m_description = task.getDescription();

    if (m_metaProperties == null) {
      m_metaProperties = new HashMap<String, TaskProperty>();
    }

    if (task.getMetaProperties() != null) {
      for (final MetaProperty property : task.getMetaProperties().values()) {
        TaskProperty taskProperty = m_metaProperties.get(property.getName());

        if (taskProperty == null) {
          taskProperty = new TaskProperty();
          taskProperty.setName(property.getName());
          taskProperty.setTask(this);

          m_metaProperties.put(property.getName(), taskProperty);
        }

        switch (MetaType.valueOf(property.getType())) {
          case STRING: {
            taskProperty.setStrValue(property.getValue());
            break;
          }

          case INTEGER: {
            taskProperty.setIntValue(Integer.parseInt(property.getValue()));
            break;
          }

          case BOOLEAN: {
            taskProperty.setBoolValue(Boolean.parseBoolean(property.getValue()));
            break;
          }

          case DATE: {
            try {
              taskProperty.setDateValue(MetaProperty.g_format.parse(property.getValue()));
            } catch (final ParseException e) {
            }
            break;
          }
        }
      }
    }
  }
}
