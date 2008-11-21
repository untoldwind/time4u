package de.objectcode.time4u.server.entities;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.api.data.TodoSummary;

@Entity
@Table(name = "T4U_TODOSBASE")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TodoBaseEntity
{
  /** Primary key. */
  protected String m_id;
  /** Revision number (increased every time something has changed) */
  protected long m_revision;
  /** Client id of the last modification */
  protected long m_lastModifiedByClient;
  /** Header/title of the todo. */
  protected String m_header;
  /** Description of the todo. */
  protected String m_description;
  /** Person who reported/created the todo. (optional) */
  protected PersonEntity m_reporter;
  /** Flag if the todo is deleted. */
  protected boolean m_deleted;
  /** Current state of the todo. */
  protected TodoState m_state;
  /** Flag if the todo is completed. */
  protected boolean m_completed;
  /** Timestamp when the todo was created. */
  protected Date m_createdAt;
  /** Timestamp when the todo was completed. (optional) */
  protected Date m_completedAt;
  /** Deadline of the todo (optional) */
  protected Date m_deadline;
  /** Depends on todos. */
  protected Set<TodoBaseEntity> m_dependsOn;
  /** Todos depending on this one. */
  protected Set<TodoBaseEntity> m_dependents;
  /** Teams that can see this todo. */
  protected Set<TeamEntity> m_visibleToTeams;
  /** Persons that can see this todo. */
  protected Set<PersonEntity> m_visibleToPersons;
  /** TodoGroup (optional) */
  protected TodoGroupEntity m_group;
  /** All meta properties of the todo. */
  protected Map<String, TodoMetaPropertyEntity> m_metaProperties;

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

  @Column(name = "description", length = 1000, nullable = false)
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @Column(name = "header", length = 1000, nullable = false)
  public String getHeader()
  {
    return m_header;
  }

  public void setHeader(final String header)
  {
    m_header = header;
  }

  @ManyToOne(optional = true)
  @JoinColumn(name = "reporter_id")
  public PersonEntity getReporter()
  {
    return m_reporter;
  }

  public void setReporter(final PersonEntity reporter)
  {
    m_reporter = reporter;
  }

  @Column(name = "deleted", nullable = false)
  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  @Type(type = "de.objectcode.time4u.server.entities.util.GenericEnumUserType", parameters = {
      @Parameter(name = "enumClass", value = "de.objectcode.time4u.server.api.data.TodoState"),
      @Parameter(name = "identifierMethod", value = "getCode"), @Parameter(name = "valueOfMethod", value = "forCode") })
  @Column(name = "state", nullable = false)
  public TodoState getState()
  {
    return m_state;
  }

  public void setState(final TodoState state)
  {
    m_state = state;
  }

  @Column(name = "completed", nullable = false)
  public boolean isCompleted()
  {
    return m_completed;
  }

  public void setCompleted(final boolean completed)
  {
    m_completed = completed;
  }

  @Column(name = "completedat", nullable = true)
  public Date getCompletedAt()
  {
    return m_completedAt;
  }

  public void setCompletedAt(final Date completedAt)
  {
    m_completedAt = completedAt;
  }

  @Column(name = "createdat", nullable = false)
  public Date getCreatedAt()
  {
    return m_createdAt;
  }

  public void setCreatedAt(final Date createdAt)
  {
    m_createdAt = createdAt;
  }

  @Column(name = "deadline", nullable = true)
  public Date getDeadline()
  {
    return m_deadline;
  }

  public void setDeadline(final Date deadline)
  {
    m_deadline = deadline;
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "T4U_TODOSBASE_DEPENDS", joinColumns = { @JoinColumn(name = "dependent_id") }, inverseJoinColumns = { @JoinColumn(name = "dependsOn_id") })
  public Set<TodoBaseEntity> getDependsOn()
  {
    return m_dependsOn;
  }

  public void setDependsOn(final Set<TodoBaseEntity> dependsOn)
  {
    m_dependsOn = dependsOn;
  }

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "dependsOn")
  public Set<TodoBaseEntity> getDependents()
  {
    return m_dependents;
  }

  public void setDependents(final Set<TodoBaseEntity> dependents)
  {
    m_dependents = dependents;
  }

  @MapKey(name = "name")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entityId")
  public Map<String, TodoMetaPropertyEntity> getMetaProperties()
  {
    return m_metaProperties;
  }

  public void setMetaProperties(final Map<String, TodoMetaPropertyEntity> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  @ManyToMany
  @JoinTable(name = "T4U_TODOSBASE_TO_TEAMS", joinColumns = { @JoinColumn(name = "todo_id") }, inverseJoinColumns = { @JoinColumn(name = "team_id") })
  public Set<TeamEntity> getVisibleToTeams()
  {
    return m_visibleToTeams;
  }

  public void setVisibleToTeams(final Set<TeamEntity> visibleToTeams)
  {
    m_visibleToTeams = visibleToTeams;
  }

  @ManyToMany
  @JoinTable(name = "T4U_TODOSBASE_TO_PERSONS", joinColumns = { @JoinColumn(name = "todo_id") }, inverseJoinColumns = { @JoinColumn(name = "person_id") })
  public Set<PersonEntity> getVisibleToPersons()
  {
    return m_visibleToPersons;
  }

  public void setVisibleToPersons(final Set<PersonEntity> visibleToPersons)
  {
    m_visibleToPersons = visibleToPersons;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "group_id")
  public TodoGroupEntity getGroup()
  {
    return m_group;
  }

  public void setGroup(final TodoGroupEntity group)
  {
    m_group = group;
  }

  public void toSummaryDTO(final TodoSummary todo)
  {
    todo.setId(m_id);
    todo.setRevision(m_revision);
    todo.setLastModifiedByClient(m_lastModifiedByClient);
    todo.setState(m_state);
    todo.setDeleted(m_deleted);
    todo.setHeader(m_header);
    todo.setDescription(m_description);
    todo.setCompleted(m_completed);
    todo.setCreatedAt(m_createdAt);
    todo.setCompletedAt(m_completedAt);
    todo.setDeadline(m_deadline);
    todo.setReporterId(m_reporter != null ? m_reporter.getId() : null);
    todo.setGroupdId(m_group != null ? m_group.getId() : null);
  }
}
