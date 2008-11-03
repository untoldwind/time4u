package de.objectcode.time4u.server.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.entities.context.IPersistenceContext;

@Entity
@Table(name = "T4U_TODOSGROUPS")
public class TodoGroupEntity extends TodoBaseEntity
{
  /** Todos that a port of this group. */
  private Set<TodoBaseEntity> m_parts;

  /**
   * Default constructor for hibernate.
   */
  protected TodoGroupEntity()
  {
  }

  public TodoGroupEntity(final String id, final long revision, final long lastModifiedByClient)
  {
    m_id = id;
    m_revision = revision;
    m_lastModifiedByClient = lastModifiedByClient;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
  public Set<TodoBaseEntity> getParts()
  {
    return m_parts;
  }

  public void setParts(final Set<TodoBaseEntity> parts)
  {
    m_parts = parts;
  }

  @Override
  public void toSummaryDTO(final TodoSummary todo)
  {
    super.toSummaryDTO(todo);

    todo.setGroup(true);
  }

  public void toDTO(final TodoGroup todoGroup)
  {
    toSummaryDTO(todoGroup);

    final List<String> visibleToPersonIds = new ArrayList<String>();
    for (final PersonEntity person : m_visibleToPersons) {
      visibleToPersonIds.add(person.getId());
    }
    todoGroup.setVisibleToPersonIds(visibleToPersonIds);
    final List<String> visibleToTeamIds = new ArrayList<String>();
    for (final TeamEntity team : m_visibleToTeams) {
      visibleToTeamIds.add(team.getId());
    }
    todoGroup.setVisibleToTeamIds(visibleToTeamIds);

    final List<String> partIds = new ArrayList<String>();
    if (m_parts != null) {
      for (final TodoBaseEntity todo : m_parts) {
        partIds.add(todo.getId());
      }
    }
    todoGroup.setPartIds(partIds);

    if (m_metaProperties != null) {
      for (final TodoMetaPropertyEntity property : m_metaProperties.values()) {
        todoGroup.setMetaProperty(property.toDTO());
      }
    }
  }

  public void fromDTO(final IPersistenceContext context, final TodoGroup todoGroup)
  {
    m_lastModifiedByClient = todoGroup.getLastModifiedByClient();
    m_createdAt = todoGroup.getCreatedAt();
    m_header = todoGroup.getHeader();
    m_description = todoGroup.getDescription();
    m_state = todoGroup.getState();

    if (todoGroup.getReporterId() != null) {
      m_reporter = context.findPerson(todoGroup.getReporterId(), todoGroup.getLastModifiedByClient());
    } else {
      m_reporter = null;
    }
    m_completed = todoGroup.isCompleted();
    m_completedAt = todoGroup.getCompletedAt();
    m_deadline = todoGroup.getDeadline();
    if (todoGroup.getGroupdId() != null) {
      m_group = context.findTodoGroup(todoGroup.getId(), todoGroup.getLastModifiedByClient());
    } else {
      m_group = null;
    }

    if (m_visibleToTeams == null) {
      m_visibleToTeams = new HashSet<TeamEntity>();
    }
    m_visibleToTeams.clear();
    if (todoGroup.getVisibleToTeamIds() != null) {
      for (final String teamId : todoGroup.getVisibleToTeamIds()) {
        m_visibleToTeams.add(context.findTeam(teamId, todoGroup.getLastModifiedByClient()));
      }
    }

    if (m_visibleToPersons == null) {
      m_visibleToPersons = new HashSet<PersonEntity>();
    }
    m_visibleToPersons.clear();
    if (todoGroup.getVisibleToPersonIds() != null) {
      for (final String personId : todoGroup.getVisibleToPersonIds()) {
        m_visibleToPersons.add(context.findPerson(personId, todoGroup.getLastModifiedByClient()));
      }
    }

    if (m_metaProperties == null) {
      m_metaProperties = new HashMap<String, TodoMetaPropertyEntity>();
    }

    if (todoGroup.getMetaProperties() != null) {
      for (final MetaProperty metaProperty : todoGroup.getMetaProperties().values()) {
        TodoMetaPropertyEntity property = m_metaProperties.get(metaProperty.getName());

        if (property == null) {
          property = new TodoMetaPropertyEntity();

          m_metaProperties.put(metaProperty.getName(), property);
        }
        property.fromDTO(metaProperty);
      }
    }
  }
}
