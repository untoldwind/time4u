package de.objectcode.time4u.server.entities.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TodoEntity;

/**
 * Todo history entity. This is part of the audit information.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TODOS_HISTORY")
public class TodoHistoryEntity
{
  /** Primary key. */
  private long m_id;
  /** The todo the history entry belongs to. */
  private TodoEntity m_todo;
  /** Person who performed the change. */
  private PersonEntity m_performedBy;
  /** Timestamp of the change. */
  private Date m_performedAt;
  /** Original task. */
  private TaskEntity m_task;
  /** Original person the todo was assigned to. */
  private PersonEntity m_assignedToPerson;
  /** Original team the todo was assigned to. */
  private TeamEntity m_assignedToTeam;
  /** Original creator of the todo. */
  private PersonEntity m_reporter;
  /** Original priority. */
  private int m_priority;
  /** Original header. */
  private String m_header;
  /** Original description. */
  private String m_description;
  /** Original compeleted flag. */
  private boolean m_completed;
  /** Original timestamp the todo was created. */
  private Date m_createdAt;
  /** Original timestamp the todo was completed. */
  private Date m_completedAt;
  /** Original deadline of the todo. */
  private Date m_deadline;

  public TodoHistoryEntity()
  {
  }

  public TodoHistoryEntity(final TodoEntity todo, final PersonEntity performedBy)
  {
    m_todo = todo;
    m_performedBy = performedBy;
    m_performedAt = new Date();

    m_task = todo.getTask();
    m_assignedToPerson = todo.getAssignedToPerson();
    m_assignedToTeam = todo.getAssignedToTeam();
    m_reporter = todo.getReporter();
    m_priority = todo.getPriority();
    m_header = todo.getHeader();
    m_description = todo.getDescription();
    m_completed = todo.isCompleted();
    m_createdAt = todo.getCreatedAt();
    m_completedAt = todo.getCompletedAt();
    m_deadline = todo.getDeadline();
  }

  @Id
  @GeneratedValue(generator = "SEQ_T4U_TODOS_HISTORY")
  @GenericGenerator(name = "SEQ_T4U_TODOS_HISTORY", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_TODOS_HISTORY"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "person_clientId"), @JoinColumn(name = "person_localId") })
  public PersonEntity getAssignedToPerson()
  {
    return m_assignedToPerson;
  }

  public void setAssignedToPerson(final PersonEntity assignedToPerson)
  {
    m_assignedToPerson = assignedToPerson;
  }

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "team_clientId"), @JoinColumn(name = "team_localId") })
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
  @JoinColumns( { @JoinColumn(name = "reporter_clientId"), @JoinColumn(name = "reporter_localId") })
  public PersonEntity getReporter()
  {
    return m_reporter;
  }

  public void setReporter(final PersonEntity reporter)
  {
    m_reporter = reporter;
  }

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "task_clientId"), @JoinColumn(name = "task_localId") })
  public TaskEntity getTask()
  {
    return m_task;
  }

  public void setTask(final TaskEntity task)
  {
    m_task = task;
  }

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "todo_clientId"), @JoinColumn(name = "todo_localId") })
  public TodoEntity getTodo()
  {
    return m_todo;
  }

  public void setTodo(final TodoEntity todo)
  {
    m_todo = todo;
  }

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "performedBy_clientId"), @JoinColumn(name = "performedBy_localId") })
  public PersonEntity getPerformedBy()
  {
    return m_performedBy;
  }

  public void setPerformedBy(final PersonEntity performedBy)
  {
    m_performedBy = performedBy;
  }

  @Column(nullable = false)
  public Date getPerformedAt()
  {
    return m_performedAt;
  }

  public void setPerformedAt(final Date performedAt)
  {
    m_performedAt = performedAt;
  }
}
