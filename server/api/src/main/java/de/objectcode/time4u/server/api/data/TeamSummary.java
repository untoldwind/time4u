package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlType;

/**
 * Team summary DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "team-summary")
public class TeamSummary implements ISynchronizableData
{
  private static final long serialVersionUID = 2474866300948380742L;

  /** Internal server id of the team. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Team name. */
  private String m_name;

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

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }
}
