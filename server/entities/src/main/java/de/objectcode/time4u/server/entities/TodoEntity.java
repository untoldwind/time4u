package de.objectcode.time4u.server.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import de.objectcode.time4u.server.api.data.TodoAssignment;
import de.objectcode.time4u.server.api.data.TodoSummary;
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
    m_estimatedTime = todo.getEstimatedTime();

    if (todo.getReporterId() != null) {
      m_reporter = context.findPerson(todo.getReporterId(), todo.getLastModifiedByClient());
    } else {
      m_reporter = null;
    }
    m_completed = todo.isCompleted();
    m_completedAt = todo.getCompletedAt();
    m_deadline = todo.getDeadline();
    if (todo.getGroupdId() != null) {
      m_group = context.findTodoGroup(todo.getId(), todo.getLastModifiedByClient());
    } else {
      m_group = null;
    }

    if (m_visibleToTeams == null) {
      m_visibleToTeams = new HashSet<TeamEntity>();
    }
    m_visibleToTeams.clear();
    if (todo.getVisibleToTeamIds() != null) {
      for (final String teamId : todo.getVisibleToTeamIds()) {
        m_visibleToTeams.add(context.findTeam(teamId, todo.getLastModifiedByClient()));
      }
    }

    if (m_visibleToPersons == null) {
      m_visibleToPersons = new HashSet<PersonEntity>();
    }
    m_visibleToPersons.clear();
    if (todo.getVisibleToPersonIds() != null) {
      for (final String personId : todo.getVisibleToPersonIds()) {
        m_visibleToPersons.add(context.findPerson(personId, todo.getLastModifiedByClient()));
      }
    }

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

  @Override
  public void toSummaryDTO(final TodoSummary todo)
  {
    super.toSummaryDTO(todo);

    todo.setGroup(false);
    todo.setState(m_state);
  }

  public void toDTO(final Todo todo)
  {
    toSummaryDTO(todo);

    todo.setTaskId(m_task.getId());
    todo.setPriority(m_priority);
    todo.setEstimatedTime(m_estimatedTime);

    final List<TodoAssignment> assignments = new ArrayList<TodoAssignment>();
    for (final TodoAssignmentEntity assignementEntity : m_assignments) {
      final TodoAssignment assignment = new TodoAssignment();

      assignment.setPersonId(assignementEntity.getPersonId());
      assignment.setEstimatedTime(assignementEntity.getEstimatedTime());
    }
    todo.setAssignments(assignments);

    final List<String> visibleToPersonIds = new ArrayList<String>();
    for (final PersonEntity person : m_visibleToPersons) {
      visibleToPersonIds.add(person.getId());
    }
    todo.setVisibleToPersonIds(visibleToPersonIds);
    final List<String> visibleToTeamIds = new ArrayList<String>();
    for (final TeamEntity team : m_visibleToTeams) {
      visibleToTeamIds.add(team.getId());
    }
    todo.setVisibleToTeamIds(visibleToTeamIds);

    if (m_metaProperties != null) {
      for (final TodoMetaPropertyEntity property : m_metaProperties.values()) {
        todo.setMetaProperty(property.toDTO());
      }
    }
  }
}
