package de.objectcode.time4u.server.ejb.seam.api;

import java.sql.Date;

public class WorkItemData
{
  private final Date m_day;
  private final int m_begin;
  private final int m_end;
  private final String m_comment;
  private final String m_personId;
  private final String m_projectId;
  private final String[] m_projectPath;
  private final String m_taskId;

  public WorkItemData(final Date day, final int begin, final int end, final String comment, final String personId,
      final String projectId, final String[] projectPath, final String taskId)
  {
    m_day = day;
    m_begin = begin;
    m_end = end;
    m_comment = comment;
    m_personId = personId;
    m_projectId = projectId;
    m_projectPath = projectPath;
    m_taskId = taskId;
  }

  public Date getDay()
  {
    return m_day;
  }

  public int getBegin()
  {
    return m_begin;
  }

  public int getEnd()
  {
    return m_end;
  }

  public String getComment()
  {
    return m_comment;
  }

  public String getPersonId()
  {
    return m_personId;
  }

  public String getProjectId()
  {
    return m_projectId;
  }

  public String[] getProjectPath()
  {
    return m_projectPath;
  }

  public String getTaskId()
  {
    return m_taskId;
  }

}
