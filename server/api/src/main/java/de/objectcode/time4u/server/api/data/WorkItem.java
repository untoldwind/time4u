package de.objectcode.time4u.server.api.data;

import java.util.Date;

/**
 * WorkItem DTO object.
 * 
 * @author junglas
 */
public class WorkItem implements ISynchronizableData, Comparable<WorkItem>
{
  private static final long serialVersionUID = -3834849041983216580L;

  /** Internal server id of the workitem. */
  private long m_id;
  /** Revision number. */
  private long m_revision;
  /** Server id of the person owning the workitem. */
  private long m_personId;
  /** Server id of the project the workitem belongs to. */
  private long m_projectId;
  /** Server id of the task the workitem belongs to. */
  private long m_taskId;
  /** Timestamp when the workitem begun. */
  private Date m_begin;
  /** Timestamp when the workitem ended. */
  private Date m_end;
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

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
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
  public Date getBegin()
  {
    return m_begin;
  }

  /**
   * @param begin
   *          the begin to set
   */
  public void setBegin(final Date begin)
  {
    m_begin = begin;
  }

  /**
   * @return the end
   */
  public Date getEnd()
  {
    return m_end;
  }

  /**
   * @param end
   *          the end to set
   */
  public void setEnd(final Date end)
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

  public long getPersonId()
  {
    return m_personId;
  }

  public void setPersonId(final long personId)
  {
    m_personId = personId;
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

    if (m_begin.compareTo(o.m_begin) != 0) {
      return m_begin.compareTo(o.m_begin);
    }
    if (m_end.compareTo(o.m_end) != 0) {
      return m_end.compareTo(o.m_end);
    }

    if (m_id != o.m_id) {
      return m_id < o.m_id ? -1 : 1;
    }

    return 0;
  }
}
