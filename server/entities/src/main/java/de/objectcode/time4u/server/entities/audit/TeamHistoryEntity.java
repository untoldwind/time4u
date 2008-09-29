package de.objectcode.time4u.server.entities.audit;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TeamEntity;

/**
 * Team history entity. This is part of the audit infomation.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TEAMS_HISTORY")
public class TeamHistoryEntity
{
  /** Primary key. */
  private long m_id;
  /** The team this history entry belongs to. */
  private TeamEntity m_team;
  /** Person who performed the change. */
  private PersonEntity m_performedBy;
  /** Timestamp of the change. */
  private Date m_performedAt;
  /** Orginial name of the team. */
  private String m_name;
  /** Original owners of the team. */
  private Set<PersonEntity> m_owners;
  /** Original members of the team. */
  private Set<PersonEntity> m_members;

  public TeamHistoryEntity()
  {
  }

  public TeamHistoryEntity(final TeamEntity team, final PersonEntity performedBy)
  {
    m_team = team;
    m_performedBy = performedBy;
    m_performedAt = new Date();

    m_owners = new HashSet<PersonEntity>(team.getOwners());
    m_name = team.getName();
    m_members = new HashSet<PersonEntity>(team.getMembers());
  }

  @Id
  @GeneratedValue(generator = "SEQ_T4U_TEAMS_HISTORY")
  @GenericGenerator(name = "SEQ_T4U_TEAMS_HISTORY", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_TEAMS_HISTORY"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "team_id")
  public TeamEntity getTeam()
  {
    return m_team;
  }

  public void setTeam(final TeamEntity team)
  {
    m_team = team;
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
  @JoinTable(name = "T4U_TEAMS_HISTORY_OWNERS", joinColumns = { @JoinColumn(name = "teamhistory_id") }, inverseJoinColumns = { @JoinColumn(name = "person_id") })
  public Set<PersonEntity> getOwner()
  {
    return m_owners;
  }

  public void setOwner(final Set<PersonEntity> owners)
  {
    m_owners = owners;
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "T4U_TEAMS_HISTORY_MEMBERS", joinColumns = { @JoinColumn(name = "teamhistory_id") }, inverseJoinColumns = { @JoinColumn(name = "person_id") })
  public Set<PersonEntity> getMembers()
  {
    return m_members;
  }

  public void setMembers(final Set<PersonEntity> members)
  {
    m_members = members;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "performedBy_id")
  public PersonEntity getPerformedBy()
  {
    return m_performedBy;
  }

  public void setPerformedBy(final PersonEntity performedBy)
  {
    m_performedBy = performedBy;
  }

  @Column(nullable = false)
  public Date getPerformedAt()
  {
    return m_performedAt;
  }

  public void setPerformedAt(final Date performedAt)
  {
    m_performedAt = performedAt;
  }
}
