package de.objectcode.time4u.server.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.entities.context.IPersistenceContext;

/**
 * Team entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TEAMS")
public class TeamEntity implements IdAndNameAwareEntity
{
  /** Primary key. */
  private String m_id;
  /** Team name. */
  private String m_name;
  /** Description of the team. */
  private String m_description;
  /** Flag if the team is deleted. */
  private boolean m_deleted;
  /** All owners (responsible persons) of the team. */
  private Set<PersonEntity> m_owners;
  /** All members of the team. */
  private Set<PersonEntity> m_members;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Meta properties of the team */
  Map<String, TeamMetaPropertyEntity> m_metaProperties;

  /**
   * Default construtor for hibernate.
   */
  protected TeamEntity()
  {
  }

  public TeamEntity(final String id, final long revision, final long lastModifiedByClient, final String name)
  {
    m_id = id;
    m_revision = revision;
    m_lastModifiedByClient = lastModifiedByClient;
    m_name = name;
    m_owners = new HashSet<PersonEntity>();
    m_members = new HashSet<PersonEntity>();
  }

  @Id
  @Column(length = 36)
  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
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

  @Column(length = 1000, nullable = true)
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
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

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  @MapKey(name = "name")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entityId")
  public Map<String, TeamMetaPropertyEntity> getMetaProperties()
  {
    return m_metaProperties;
  }

  public void setMetaProperties(final Map<String, TeamMetaPropertyEntity> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  @Override
  public int hashCode()
  {
    return m_id.hashCode();
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

    return m_id.equals(castObj.m_id);
  }

  public void toSummaryDTO(final TeamSummary team)
  {
    team.setId(m_id);
    team.setRevision(m_revision);
    team.setLastModifiedByClient(m_lastModifiedByClient);
    team.setName(m_name);
    team.setDescription(m_description);
    team.setDeleted(m_deleted);
  }

  public void toDTO(final Team team)
  {
    toSummaryDTO(team);

    final List<String> ownerIds = new ArrayList<String>();

    for (final PersonEntity person : m_owners) {
      ownerIds.add(person.getId());
    }
    team.setOwnerIds(ownerIds);
    final List<String> memberIds = new ArrayList<String>();

    for (final PersonEntity person : m_members) {
      memberIds.add(person.getId());
    }
    team.setMemberIds(memberIds);

    if (m_metaProperties != null) {
      for (final TeamMetaPropertyEntity property : m_metaProperties.values()) {
        team.setMetaProperty(property.toDTO());
      }
    }
  }

  public void fromDTO(final IPersistenceContext context, final Team team)
  {
    m_lastModifiedByClient = team.getLastModifiedByClient();
    m_name = team.getName();
    m_description = team.getDescription();
    m_deleted = team.isDeleted();

    if (m_owners == null) {
      m_owners = new HashSet<PersonEntity>();
    }
    m_owners.clear();
    if (team.getOwnerIds() != null) {
      for (final String id : team.getOwnerIds()) {
        m_owners.add(context.findPerson(id, team.getLastModifiedByClient()));
      }
    }

    if (m_members == null) {
      m_members = new HashSet<PersonEntity>();
    }

    m_members.clear();
    if (team.getMemberIds() != null) {
      for (final String id : team.getMemberIds()) {
        m_members.add(context.findPerson(id, team.getLastModifiedByClient()));
      }
    }

    if (m_metaProperties == null) {
      m_metaProperties = new HashMap<String, TeamMetaPropertyEntity>();
    }

    if (team.getMetaProperties() != null) {
      for (final MetaProperty metaProperty : team.getMetaProperties().values()) {
        TeamMetaPropertyEntity property = m_metaProperties.get(metaProperty.getName());

        if (property == null) {
          property = new TeamMetaPropertyEntity();

          m_metaProperties.put(metaProperty.getName(), property);
        }
        property.fromDTO(metaProperty);
      }
    }
  }
}
