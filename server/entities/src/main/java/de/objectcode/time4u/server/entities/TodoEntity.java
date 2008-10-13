package de.objectcode.time4u.server.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.Todo;

/**
 * Todo entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TODOS")
public class TodoEntity
{
  /** Primary key. */
  private String m_id;
  /** The task the todo belongs to. */
  private TaskEntity m_task;
  /** Person the todo is assigned to (optional) */
  private PersonEntity m_assignedToPerson;
  /** Team the todo is assigned to (optional) */
  private TeamEntity m_assignedToTeam;
  /** Person who created the todo. (optional) */
  private PersonEntity m_reporter;
  /** Priority of the todo. */
  private int m_priority;
  /** Header/title of the todo. */
  private String m_header;
  /** Description of the todo. */
  private String m_description;
  /** Flag if the todo is completed. */
  private boolean m_completed;
  /** Timestamp when the todo was created. */
  private Date m_createdAt;
  /** Timestamp when the todo was completed. (optional) */
  private Date m_completedAt;
  /** Deadline of the todo (optional) */
  private Date m_deadline;
  /** All meta properties of the todo. */
  private Map<String, TodoMetaPropertyEntity> m_metaProperties;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;

  /**
   * Default constructor for hibernate.
   */
  protected TodoEntity()
  {
  }

  public TodoEntity(final String id, final long revision, final long lastModifiedByClient)
  {
    m_id = id;
    m_revision = revision;
    m_lastModifiedByClient = lastModifiedByClient;
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

  @ManyToOne
  @JoinColumn(name = "person_id")
  public PersonEntity getAssignedToPerson()
  {
    return m_assignedToPerson;
  }

  public void setAssignedToPerson(final PersonEntity assignedToPerson)
  {
    m_assignedToPerson = assignedToPerson;
  }

  @ManyToOne
  @JoinColumn(name = "team_id")
  public TeamEntity getAssignedToTeam()
  {
    return m_assignedToTeam;
  }

  public void setAssignedToTeam(final TeamEntity assignedToTeam)
  {
    m_assignedToTeam = assignedToTeam;
  }

  @Column(name = "completed", nullable = false)
  public boolean isCompleted()
  {
    return m_completed;
  }

  public void setCompleted(final boolean completed)
  {
    m_completed = completed;
  }

  @Column(name = "completedat", nullable = true)
  public Date getCompletedAt()
  {
    return m_completedAt;
  }

  public void setCompletedAt(final Date completedAt)
  {
    m_completedAt = completedAt;
  }

  @Column(name = "createdat", nullable = false)
  public Date getCreatedAt()
  {
    return m_createdAt;
  }

  public void setCreatedAt(final Date createdAt)
  {
    m_createdAt = createdAt;
  }

  @Column(name = "deadline", nullable = true)
  public Date getDeadline()
  {
    return m_deadline;
  }

  public void setDeadline(final Date deadline)
  {
    m_deadline = deadline;
  }

  @Column(name = "description", length = 1000, nullable = false)
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @Column(name = "header", length = 1000, nullable = false)
  public String getHeader()
  {
    return m_header;
  }

  public void setHeader(final String header)
  {
    m_header = header;
  }

  @Column(name = "priority", nullable = false)
  public int getPriority()
  {
    return m_priority;
  }

  public void setPriority(final int priority)
  {
    m_priority = priority;
  }

  @ManyToOne
  @JoinColumn(name = "reporter_id")
  public PersonEntity getReporter()
  {
    return m_reporter;
  }

  public void setReporter(final PersonEntity reporter)
  {
    m_reporter = reporter;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "task_id")
  public TaskEntity getTask()
  {
    return m_task;
  }

  public void setTask(final TaskEntity task)
  {
    m_task = task;
  }

  @MapKey(name = "name")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entityId")
  public Map<String, TodoMetaPropertyEntity> getMetaProperties()
  {
    return m_metaProperties;
  }

  public void setMetaProperties(final Map<String, TodoMetaPropertyEntity> metaProperties)
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

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public void fromDTO(final EntityManager entityManager, final Todo todo)
  {
    m_lastModifiedByClient = todo.getLastModifiedByClient();
    m_task = entityManager.find(TaskEntity.class, todo.getTaskId());
    m_createdAt = todo.getCreatedAt();
    m_header = todo.getHeader();
    m_description = todo.getDescription();
    m_priority = todo.getPriority();

    if (todo.getReporterId() != null) {
      m_reporter = entityManager.find(PersonEntity.class, todo.getReporterId());
    } else {
      m_reporter = null;
    }
    if (todo.getAssignedToPersonId() != null) {
      m_assignedToPerson = entityManager.find(PersonEntity.class, todo.getAssignedToPersonId());
    } else {
      m_assignedToPerson = null;
    }
    if (todo.getAssignedToTeamId() != null) {
      m_assignedToTeam = entityManager.find(TeamEntity.class, todo.getAssignedToTeamId());
    } else {
      m_assignedToTeam = null;
    }
    m_completed = todo.isCompleted();
    m_completedAt = todo.getCompletedAt();
    m_deadline = todo.getDeadline();

    if (m_metaProperties == null) {
      m_metaProperties = new HashMap<String, TodoMetaPropertyEntity>();
    }

    if (todo.getMetaProperties() != null) {
      for (final MetaProperty metaProperty : todo.getMetaProperties().values()) {
        TodoMetaPropertyEntity property = m_metaProperties.get(metaProperty.getName());

        if (property == null) {
          property = new TodoMetaPropertyEntity();

          m_metaProperties.put(metaProperty.getName(), property);
        }
        property.fromDTO(metaProperty);
      }
    }
  }

  public void toDTO(final Todo todo)
  {
    todo.setId(m_id);
    todo.setRevision(m_revision);
    todo.setLastModifiedByClient(m_lastModifiedByClient);
    todo.setTaskId(m_task.getId());
    todo.setCreatedAt(m_createdAt);
    if (m_reporter != null) {
      todo.setReporterId(m_reporter.getId());
    } else {
      todo.setReporterId(null);
    }
    if (m_assignedToPerson != null) {
      todo.setAssignedToPersonId(m_assignedToPerson.getId());
    } else {
      todo.setAssignedToPersonId(null);
    }
    if (m_assignedToTeam != null) {
      todo.setAssignedToTeamId(m_assignedToTeam.getId());
    } else {
      todo.setAssignedToTeamId(null);
    }
    todo.setCompleted(m_completed);
    todo.setCompletedAt(m_completedAt);
    todo.setHeader(m_header);
    todo.setDescription(m_description);
    todo.setDeadline(m_deadline);
    todo.setPriority(m_priority);

    if (m_metaProperties != null) {
      for (final TodoMetaPropertyEntity property : m_metaProperties.values()) {
        todo.setMetaProperty(property.toDTO());
      }
    }
  }
}
