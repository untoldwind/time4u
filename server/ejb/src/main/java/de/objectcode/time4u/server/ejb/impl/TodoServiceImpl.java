package de.objectcode.time4u.server.ejb.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.objectcode.time4u.server.api.ITodoService;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;
import de.objectcode.time4u.server.entities.TodoBaseEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.TodoGroupEntity;
import de.objectcode.time4u.server.entities.context.EntityManagerPersistenceContext;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

/**
 * EJB implementation of the ITodoService interface.
 * 
 * @author junglas
 */
@Stateless
@Remote(ITodoService.class)
@org.jboss.annotation.ejb.RemoteBinding(jndiBinding = "time4u-server/TodoService/remote")
@org.jboss.ejb3.annotation.RemoteBinding(jndiBinding = "time4u-server/TodoService/remote")
public class TodoServiceImpl implements ITodoService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private ILocalIdGenerator m_idGenerator;

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public FilterResult<TodoSummary> getTodos(final TodoFilter filter)
  {
    final Query query = createQuery(filter);
    final List<TodoSummary> result = new ArrayList<TodoSummary>();

    for (final Object row : query.getResultList()) {
      if (row instanceof TodoEntity) {
        final Todo todo = new Todo();

        ((TodoEntity) row).toDTO(todo);

        result.add(todo);
      } else if (row instanceof TodoGroupEntity) {
        final TodoGroup todoGroup = new TodoGroup();

        ((TodoGroupEntity) row).toDTO(todoGroup);

        result.add(todoGroup);
      }
    }

    return new FilterResult<TodoSummary>(result);
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public FilterResult<TodoSummary> getTodoSummaries(final TodoFilter filter)
  {
    final Query query = createQuery(filter);
    final List<TodoSummary> result = new ArrayList<TodoSummary>();

    for (final Object row : query.getResultList()) {
      final TodoSummary todoSummary = new TodoSummary();

      ((TodoBaseEntity) row).toSummaryDTO(todoSummary);

      result.add(todoSummary);
    }

    return new FilterResult<TodoSummary>(result);
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public Todo storeTodo(final Todo todo)
  {
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.TODO, null);

    TodoEntity todoEntity = null;

    if (todo.getId() != null) {
      todoEntity = m_manager.find(TodoEntity.class, todo.getId());
    } else {
      todo.setId(m_idGenerator.generateLocalId(EntityType.TODO));
    }
    if (todoEntity != null) {
      todoEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), todo);
      todoEntity.setRevision(revisionLock.getLatestRevision());
    } else {
      todoEntity = new TodoEntity(todo.getId(), revisionLock.getLatestRevision(), todo.getLastModifiedByClient(), todo
          .getHeader());

      todoEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), todo);

      m_manager.persist(todoEntity);
    }

    final Todo result = new Todo();

    todoEntity.toDTO(result);

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public TodoGroup storeTodoGroup(final TodoGroup todoGroup)
  {
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.TODO, null);

    TodoGroupEntity todoEntity = null;

    if (todoGroup.getId() != null) {
      todoEntity = m_manager.find(TodoGroupEntity.class, todoGroup.getId());
    } else {
      todoGroup.setId(m_idGenerator.generateLocalId(EntityType.TODO));
    }
    if (todoEntity != null) {
      todoEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), todoGroup);
      todoEntity.setRevision(revisionLock.getLatestRevision());
    } else {
      todoEntity = new TodoGroupEntity(todoGroup.getId(), revisionLock.getLatestRevision(), todoGroup
          .getLastModifiedByClient(), todoGroup.getHeader());

      todoEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), todoGroup);

      m_manager.persist(todoEntity);
    }

    final TodoGroup result = new TodoGroup();

    todoEntity.toDTO(result);

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public TodoSummary getTodo(final String todoId)
  {
    final TodoBaseEntity todoEntity = m_manager.find(TodoBaseEntity.class, todoId);

    if (todoEntity != null) {
      if (todoEntity instanceof TodoEntity) {
        final Todo todo = new Todo();

        ((TodoEntity) todoEntity).toDTO(todo);

        return todo;
      } else if (todoEntity instanceof TodoGroupEntity) {
        final TodoGroup todoGroup = new TodoGroup();

        ((TodoGroupEntity) todoEntity).toDTO(todoGroup);

        return todoGroup;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public TodoSummary getTodoSummary(final String todoId)
  {
    final TodoBaseEntity todoEntity = m_manager.find(TodoBaseEntity.class, todoId);

    if (todoEntity != null) {
      final TodoSummary todoSummary = new TodoSummary();

      todoEntity.toSummaryDTO(todoSummary);

      return todoSummary;
    }

    return null;
  }

  private Query createQuery(final TodoFilter filter)
  {
    String combineStr = " where ";
    final StringBuffer queryStr = new StringBuffer("from " + TodoBaseEntity.class.getName() + " t");

    if (filter.getDeleted() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.deleted = :deleted");
      combineStr = " and ";
    }
    if (filter.getMinRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.revision >= :minRevision");
      combineStr = " and ";
    }
    if (filter.getMaxRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.revision <= :maxRevision");
      combineStr = " and ";
    }
    if (filter.getLastModifiedByClient() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.lastModifiedByClient = :lastModifiedByClient");
      combineStr = " and ";
    }
    if (filter.getGroupId() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.group.id = :groupId");
      combineStr = " and ";
    }
    if (filter.getTaskId() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.task.id = :taskId");
      combineStr = " and ";
    }
    if (filter.getTodoStates() != null && filter.getTodoStates().length > 0) {
      queryStr.append(combineStr);
      queryStr.append("t.state in :todoStates");
      combineStr = " and ";
    }

    switch (filter.getOrder()) {
      case ID:
        queryStr.append(" order by t.id asc");
        break;
      case HEADER:
        queryStr.append(" order by t.header asc, t.id asc");
        break;
    }

    final Query query = m_manager.createQuery(queryStr.toString());

    if (filter.getDeleted() != null) {
      query.setParameter("deleted", filter.getDeleted());
    }
    if (filter.getMinRevision() != null) {
      query.setParameter("minRevision", filter.getMinRevision());
    }
    if (filter.getMaxRevision() != null) {
      query.setParameter("maxRevision", filter.getMaxRevision());
    }
    if (filter.getLastModifiedByClient() != null) {
      query.setParameter("lastModifiedByClient", filter.getLastModifiedByClient());
    }
    if (filter.getGroupId() != null) {
      query.setParameter("groupId", filter.getGroupId());
    }
    if (filter.getTaskId() != null) {
      query.setParameter("taskid", filter.getTaskId());
    }
    if (filter.getTodoStates() != null && filter.getTodoStates().length > 0) {
      query.setParameter("todoStates", filter.getTodoStates());
    }

    return query;
  }

}
