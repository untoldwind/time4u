package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Team summary DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "team-summary")
@XmlRootElement(name = "team-summary")
public class TeamSummary implements ISynchronizableData
{
  private static final long serialVersionUID = 2474866300948380742L;

  /** Internal server id of the team. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Flag if the team is deleted. */
  private boolean m_deleted;
  /** Team name. */
  private String m_name;
  /** Description of the team. */
  private String m_description;

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

  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return m_id != null ? m_id.hashCode() : 0;
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

    if (obj == null || !(obj instanceof TeamSummary)) {
      return false;
    }

    final TeamSummary castObj = (TeamSummary) obj;

    if (m_id == null) {
      return false;
    }

    return m_id.equals(castObj.m_id);
  }
}
