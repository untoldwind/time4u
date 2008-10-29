package de.objectcode.time4u.server.entities;

import java.util.HashMap;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.entities.context.IPersistenceContext;

/**
 * Todo entity.
 * 
 * @author junglas
 */
@Entity
@DiscriminatorValue("t")
public class TodoEntity extends TodoBaseEntity
{
  /** The task the todo belongs to. */
  private TaskEntity m_task;
  /** Priority of the todo. */
  private int m_priority;
  /** Estimated time. */
  private Integer m_estimatedTime;
  /** Assignments */
  private Set<TodoAssignmentEntity> m_assignments;

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

  @Column(name = "priority", nullable = false)
  public int getPriority()
  {
    return m_priority;
  }

  public void setPriority(final int priority)
  {
    m_priority = priority;
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

  public Integer getEstimatedTime()
  {
    return m_estimatedTime;
  }

  public void setEstimatedTime(final Integer estimatedTime)
  {
    m_estimatedTime = estimatedTime;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "todo")
  public Set<TodoAssignmentEntity> getAssignments()
  {
    return m_assignments;
  }

  public void setAssignments(final Set<TodoAssignmentEntity> assignments)
  {
    m_assignments = assignments;
  }

  public void fromDTO(final IPersistenceContext context, final Todo todo)
  {
    m_lastModifiedByClient = todo.getLastModifiedByClient();
    m_task = context.findTask(todo.getTaskId(), todo.getLastModifiedByClient());
    m_createdAt = todo.getCreatedAt();
    m_header = todo.getHeader();
    m_description = todo.getDescription();
    m_priority = todo.getPriority();

    if (todo.getReporterId() != null) {
      m_reporter = context.findPerson(todo.getReporterId(), todo.getLastModifiedByClient());
    } else {
      m_reporter = null;
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
