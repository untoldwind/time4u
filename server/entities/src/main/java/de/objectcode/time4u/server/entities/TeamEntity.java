package de.objectcode.time4u.server.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.Team;

/**
 * Team entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TEAMS")
public class TeamEntity
{
  /** Primary key. */
  private long m_id;
  /** Team name. */
  private String m_name;
  /** All owners (responsible persons) of the team. */
  private Set<PersonEntity> m_owners;
  /** All members of the team. */
  private Set<PersonEntity> m_members;
  /** Revision number (increased every time something has changed) */
  private long m_revision;

  @Id
  @GeneratedValue
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @Column(length = 50, nullable = false)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "T4U_TEAMS_OWNERS", joinColumns = { @JoinColumn(name = "team_id") }, inverseJoinColumns = { @JoinColumn(name = "person_id") })
  public Set<PersonEntity> getOwners()
  {
    return m_owners;
  }

  public void setOwners(final Set<PersonEntity> owners)
  {
    m_owners = owners;
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "T4U_TEAMS_MEMBERS", joinColumns = { @JoinColumn(name = "team_id") }, inverseJoinColumns = { @JoinColumn(name = "person_id") })
  public Set<PersonEntity> getMembers()
  {
    return m_members;
  }

  public void setMembers(final Set<PersonEntity> members)
  {
    m_members = members;
  }

  public Long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final Long revision)
  {
    if (revision != null) {
      m_revision = revision;
    }
    m_revision = 0;
  }

  @Override
  public int hashCode()
  {
    return (int) (m_id ^ m_id >>> 32);
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof TeamEntity)) {
      return false;
    }

    final TeamEntity castObj = (TeamEntity) obj;

    return m_id == castObj.m_id;
  }

  public void toDTO(final Team team)
  {
    team.setId(m_id);
    team.setName(m_name);
    final List<Long> ownerIds = new ArrayList<Long>();

    for (final PersonEntity person : m_owners) {
      ownerIds.add(person.getId());
    }
    team.setOwnerIds(ownerIds);
    final List<Long> memberIds = new ArrayList<Long>();

    for (final PersonEntity person : m_members) {
      memberIds.add(person.getId());
    }
    team.setMemberIds(memberIds);
  }

  public void fromDTO(final EntityManager entityManager, final Team team)
  {
    m_name = team.getName();

    if (m_owners == null) {
      m_owners = new HashSet<PersonEntity>();
    }
    m_owners.clear();
    if (team.getOwnerIds() != null) {
      for (final long id : team.getOwnerIds()) {
        m_owners.add(entityManager.find(PersonEntity.class, id));
      }
    }

    if (m_members == null) {
      m_members = new HashSet<PersonEntity>();
    }

    m_members.clear();
    if (team.getMemberIds() != null) {
      for (final long id : team.getMemberIds()) {
        m_members.add(entityManager.find(PersonEntity.class, id));
      }
    }
  }
}
