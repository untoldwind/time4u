package de.objectcode.time4u.server.api.data;

/**
 * WorkItem DTO object.
 * 
 * @author junglas
 */
public class WorkItem implements Comparable<WorkItem>
{
  private static final long serialVersionUID = -3834849041983216580L;

  /** Internal server id of the workitem. */
  private long m_id;
  /** Server id of the day of the workitem. */
  private long m_dayInfoId;
  /** Server id of the project the workitem belongs to. */
  private long m_projectId;
  /** Server id of the task the workitem belongs to. */
  private long m_taskId;
  /** Time the workitem begun (in seconds starting from midnight 00:00:00). */
  private int m_begin;
  /** Time the workitem ended (in seconds starting from midnight 00:00:00) */
  private int m_end;
  /** Workitem comment. */
  private String m_comment;
  /** Optional server id of the todo this workitem belongs to. */
  private Long m_todoId;

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
   * @return the todoId
   */
  public long getTaskId()
  {
    return m_taskId;
  }

  /**
   * @param todoId
   *          the todoId to set
   */
  public void setTaskId(final long taskId)
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

  public long getProjectId()
  {
    return m_projectId;
  }

  public void setProjectId(final long projectId)
  {
    m_projectId = projectId;
  }

  public Long getTodoId()
  {
    return m_todoId;
  }

  public void setTodoId(final Long todoId)
  {
    m_todoId = todoId;
  }

  public long getDayInfoId()
  {
    return m_dayInfoId;
  }

  public void setDayInfoId(final long dayInfoId)
  {
    m_dayInfoId = dayInfoId;
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
    if (m_id == o.m_id) {
      return 0;
    }

    if (m_begin != o.m_begin) {
      return m_begin < o.m_begin ? -1 : 1;
    }
    if (m_end != o.m_end) {
      return m_end < o.m_end ? -1 : 1;
    }

    if (m_id != o.m_id) {
      return m_id < o.m_id ? -1 : 1;
    }

    return 0;
  }
}
