package de.objectcode.time4u.server.api.data;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

/**
 * Team DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "team")
public class Team extends TeamSummary
{
  private static final long serialVersionUID = -2393342879169250933L;

  /** List of server ids of all persons owning/administrating the team. */
  private List<String> m_ownerIds;
  /** List of server ids of all team members (person). */
  private List<String> m_memberIds;

  public List<String> getOwnerIds()
  {
    return m_ownerIds;
  }

  public void setOwnerIds(final List<String> ownerIds)
  {
    m_ownerIds = ownerIds;
  }

  public List<String> getMemberIds()
  {
    return m_memberIds;
  }

  public void setMemberIds(final List<String> memberIds)
  {
    m_memberIds = memberIds;
  }

}
