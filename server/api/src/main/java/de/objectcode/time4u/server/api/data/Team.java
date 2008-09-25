package de.objectcode.time4u.server.api.data;

import java.util.List;

/**
 * Team DTO object.
 * 
 * @author junglas
 */
public class Team implements ISynchronizableData
{
  private static final long serialVersionUID = -2393342879169250933L;

  /** Internal server id of the team. */
  private long m_id;
  /** Team name. */
  private String m_name;
  /** List of server ids of all persons owning/administrating the team. */
  private List<Long> m_ownerIds;
  /** List of server ids of all team members (person). */
  private List<Long> m_memberIds;

  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  public List<Long> getOwnerIds()
  {
    return m_ownerIds;
  }

  public void setOwnerIds(final List<Long> ownerIds)
  {
    m_ownerIds = ownerIds;
  }

  public List<Long> getMemberIds()
  {
    return m_memberIds;
  }

  public void setMemberIds(final List<Long> memberIds)
  {
    m_memberIds = memberIds;
  }

}
