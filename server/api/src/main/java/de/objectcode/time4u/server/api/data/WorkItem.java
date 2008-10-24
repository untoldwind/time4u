package de.objectcode.time4u.server.api.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * WorkItem DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "workitem")
public class WorkItem implements Comparable<WorkItem>, Serializable
{
  private static final long serialVersionUID = -3834849041983216580L;

  /** Internal server id of the workitem. */
  private String m_id;
  /** The calendar day of the workitem (this identifies the dayinfo). */
  private CalendarDay m_day;
  /** Server id of the project the workitem belongs to. */
  private String m_projectId;
  /** Server id of the task the workitem belongs to. */
  private String m_taskId;
  /** Time the workitem begun (in seconds starting from midnight 00:00:00). */
  private int m_begin;
  /** Time the workitem ended (in seconds starting from midnight 00:00:00) */
  private int m_end;
  /** Workitem comment. */
  private String m_comment;
  /** Flag if the workitem is valid. */
  private boolean m_valid;
  /** Optional server id of the todo this workitem belongs to. */
  private String m_todoId;

  /**
   * @return the id
   */
  public String getId()
  {
    return m_id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(final String id)
  {
    m_id = id;
  }

  /**
   * @return the todoId
   */
  public String getTaskId()
  {
    return m_taskId;
  }

  /**
   * @param todoId
   *          the todoId to set
   */
  public void setTaskId(final String taskId)
  {
    m_taskId = taskId;
  }

  /**
   * @return the comment
   */
  public String getComment()
  {
    return m_comment;
  }

  /**
   * @param comment
   *          the comment to set
   */
  public void setComment(final String comment)
  {
    m_comment = comment;
  }

  /**
   * @return the begin
   */
  public int getBegin()
  {
    return m_begin;
  }

  /**
   * @param begin
   *          the begin to set
   */
  public void setBegin(final int begin)
  {
    m_begin = begin;
  }

  /**
   * @return the end
   */
  public int getEnd()
  {
    return m_end;
  }

  /**
   * @param end
   *          the end to set
   */
  public void setEnd(final int end)
  {
    m_end = end;
  }

  public int getDuration()
  {
    return m_end - m_begin;
  }

  public boolean isValid()
  {
    return m_valid;
  }

  public void setValid(final boolean valid)
  {
    m_valid = valid;
  }

  public String getProjectId()
  {
    return m_projectId;
  }

  public void setProjectId(final String projectId)
  {
    m_projectId = projectId;
  }

  public String getTodoId()
  {
    return m_todoId;
  }

  public void setTodoId(final String todoId)
  {
    m_todoId = todoId;
  }

  public CalendarDay getDay()
  {
    return m_day;
  }

  public void setDay(final CalendarDay day)
  {
    m_day = day;
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

    if (obj == null || !(obj instanceof WorkItem)) {
      return false;
    }

    final WorkItem castObj = (WorkItem) obj;

    return m_id == castObj.m_id;
  }

  /**
   * {@inheritDoc}
   */
  public int compareTo(final WorkItem o)
  {
    if (m_id.equals(o.m_id)) {
      return 0;
    }

    if (m_begin != o.m_begin) {
      return m_begin < o.m_begin ? -1 : 1;
    }
    if (m_end != o.m_end) {
      return m_end < o.m_end ? -1 : 1;
    }

    return m_id.compareTo(o.m_id);
  }
}
