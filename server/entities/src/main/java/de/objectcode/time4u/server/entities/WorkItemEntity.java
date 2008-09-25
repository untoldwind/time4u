package de.objectcode.time4u.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.api.data.WorkItem;
import de.objectcode.time4u.server.entities.context.IPersistenceContext;

/**
 * Workitem entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_WORKITEMS")
public class WorkItemEntity
{
  /** Primary key. */
  private long m_id;
  /** The day of the workitem (and thereby the person the workitem belongs too). */
  private DayInfoEntity m_dayInfo;
  /** Time the workitem begun (in seconds starting from midnight 00:00:00). */
  private int m_begin;
  /** Time the workitem ended (in seconds starting from midnight 00:00:00) */
  private int m_end;
  /** Workitem comment. */
  private String m_comment;
  /** The project the workitem belongs to. */
  private ProjectEntity m_project;
  /** The task the workitem belongs to. */
  private TaskEntity m_task;
  /** The todo the workitem belongs to (optional) */
  private TodoEntity m_todo;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_WORKITEMS")
  @GenericGenerator(name = "SEQ_T4U_WORKITEMS", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_WORKITEMS"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @Column(name = "wBegin", nullable = false)
  public int getBegin()
  {
    return m_begin;
  }

  public void setBegin(final int begin)
  {
    m_begin = begin;
  }

  @Column(name = "wEnd", nullable = false)
  public int getEnd()
  {
    return m_end;
  }

  public void setEnd(final int end)
  {
    m_end = end;
  }

  @Column(name = "wComment", length = 1000, nullable = false)
  public String getComment()
  {
    return m_comment;
  }

  public void setComment(final String comment)
  {
    m_comment = comment;
  }

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  public ProjectEntity getProject()
  {
    return m_project;
  }

  public void setProject(final ProjectEntity project)
  {
    m_project = project;
  }

  @ManyToOne
  @JoinColumn(name = "task_id", nullable = false)
  public TaskEntity getTask()
  {
    return m_task;
  }

  public void setTask(final TaskEntity task)
  {
    m_task = task;
  }

  @ManyToOne
  @JoinColumn(name = "dayinfo_id", nullable = false)
  public DayInfoEntity getDayInfo()
  {
    return m_dayInfo;
  }

  public void setDayInfo(final DayInfoEntity dayInfo)
  {
    m_dayInfo = dayInfo;
  }

  @ManyToOne
  @JoinColumn(name = "todo_id", nullable = true)
  public TodoEntity getTodo()
  {
    return m_todo;
  }

  public void setTodo(final TodoEntity todo)
  {
    m_todo = todo;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof WorkItemEntity)) {
      return false;
    }

    final WorkItemEntity castObj = (WorkItemEntity) obj;

    return m_id == castObj.m_id;
  }

  public void toDTO(final WorkItem workItem)
  {
    workItem.setId(m_id);
    workItem.setBegin(m_begin);
    workItem.setEnd(m_end);
    workItem.setComment(m_comment);
    workItem.setProjectId(m_project.getId());
    workItem.setTaskId(m_task.getId());
    workItem.setDayInfoId(m_dayInfo.getId());

    if (m_todo != null) {
      workItem.setTodoId(m_todo.getId());
    } else {
      workItem.setTodoId(null);
    }
  }

  public void fromDTO(final IPersistenceContext context, final WorkItem workItem)
  {
    m_begin = workItem.getBegin();
    m_end = workItem.getEnd();

    m_comment = workItem.getComment();
    if (m_comment == null) {
      m_comment = "";
    }
    m_project = context.findProject(workItem.getProjectId());
    m_task = context.findTask(workItem.getTaskId());
    if (workItem.getTodoId() != null) {
      m_todo = context.findTodo(workItem.getTodoId());
    } else {
      m_todo = null;
    }
  }
}
