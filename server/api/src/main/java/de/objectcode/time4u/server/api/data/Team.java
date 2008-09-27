package de.objectcode.time4u.server.api.data;

import java.util.List;
import java.util.UUID;

/**
 * Team DTO object.
 * 
 * @author junglas
 */
public class Team implements ISynchronizableData
{
  private static final long serialVersionUID = -2393342879169250933L;

  /** Internal server id of the team. */
  private UUID m_id;
  /** Revision number. */
  private long m_revision;
  /** Team name. */
  private String m_name;
  /** List of server ids of all persons owning/administrating the team. */
  private List<UUID> m_ownerIds;
  /** List of server ids of all team members (person). */
  private List<UUID> m_memberIds;

  public UUID getId()
  {
    return m_id;
  }

  public void setId(final UUID id)
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

  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  public List<UUID> getOwnerIds()
  {
    return m_ownerIds;
  }

  public void setOwnerIds(final List<UUID> ownerIds)
  {
    m_ownerIds = ownerIds;
  }

  public List<UUID> getMemberIds()
  {
    return m_memberIds;
  }

  public void setMemberIds(final List<UUID> memberIds)
  {
    m_memberIds = memberIds;
  }

}
