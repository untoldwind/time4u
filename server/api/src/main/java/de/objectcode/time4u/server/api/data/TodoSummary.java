package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "todo-summary")
@XmlRootElement(name = "todo-summary")
public class TodoSummary implements ISynchronizableData
{
  private static final long serialVersionUID = 1089108700513084357L;

  /** Internal server id of the todo. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Flag if the todo is deleted */
  private boolean m_deleted;
  /** Todo header/title. */
  private String m_header;
  /** Todo description. */
  private String m_description;
  /** Optional server id of the person who created the todo. */
  private String m_reporterId;
  /** Flag if this is a summary of a todo group. */
  private boolean m_group;
  /** Todo group id */
  private String m_groupdId;
  /** State of the todo. */
  private TodoState m_state;

  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
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

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  public String getHeader()
  {
    return m_header;
  }

  public void setHeader(final String header)
  {
    m_header = header;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public String getReporterId()
  {
    return m_reporterId;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  public void setReporterId(final String reporterId)
  {
    m_reporterId = reporterId;
  }

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public boolean isGroup()
  {
    return m_group;
  }

  public void setGroup(final boolean group)
  {
    m_group = group;
  }

  public String getGroupdId()
  {
    return m_groupdId;
  }

  public void setGroupdId(final String groupdId)
  {
    m_groupdId = groupdId;
  }

  public TodoState getState()
  {
    return m_state;
  }

  public void setState(final TodoState state)
  {
    m_state = state;
  }

}
