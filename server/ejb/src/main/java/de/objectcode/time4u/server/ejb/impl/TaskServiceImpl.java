package de.objectcode.time4u.server.ejb.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.TaskFilter;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.context.EntityManagerPersistenceContext;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

/**
 * EJB3 implementation of the task service interface.
 * 
 * @author junglas
 */
@Stateless
@Remote(ITaskService.class)
@org.jboss.annotation.ejb.RemoteBinding(jndiBinding = "time4u-server/TaskService/remote")
@org.jboss.ejb3.annotation.RemoteBinding(jndiBinding = "time4u-server/TaskService/remote")
@org.jboss.annotation.security.SecurityDomain("time4u")
public class TaskServiceImpl implements ITaskService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private ILocalIdGenerator m_idGenerator;

  @Resource
  SessionContext m_sessionContext;

  public Task getTask(final String taskId)
  {
    final TaskEntity taskEntity = m_manager.find(TaskEntity.class, taskId);

    if (taskEntity != null) {
      final Task task = new Task();
      taskEntity.toDTO(task);

      return task;
    }
    return null;
  }

  public TaskSummary getTaskSummary(final String taskId)
  {
    final TaskEntity taskEntity = m_manager.find(TaskEntity.class, taskId);

    if (taskEntity != null) {
      final TaskSummary task = new TaskSummary();
      taskEntity.toSummaryDTO(task);

      return task;
    }
    return null;
  }

  public FilterResult<Task> getTasks(final TaskFilter filter)
  {
    final Query query = createQuery(filter);
    final List<Task> result = new ArrayList<Task>();

    for (final Object row : query.getResultList()) {
      final Task task = new Task();

      ((TaskEntity) row).toDTO(task);

      result.add(task);
    }

    return new FilterResult<Task>(result);
  }

  public FilterResult<TaskSummary> getTaskSummaries(final TaskFilter filter)
  {
    final Query query = createQuery(filter);
    final List<TaskSummary> result = new ArrayList<TaskSummary>();

    for (final Object row : query.getResultList()) {
      final TaskSummary task = new TaskSummary();

      ((TaskEntity) row).toSummaryDTO(task);

      result.add(task);
    }

    return new FilterResult<TaskSummary>(result);
  }

  public Task storeTask(final Task task)
  {
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.TASK, null);

    TaskEntity taskEntity = null;

    if (task.getId() != null) {
      taskEntity = m_manager.find(TaskEntity.class, task.getId());
    } else {
      task.setId(m_idGenerator.generateLocalId(EntityType.TASK));
    }
    if (taskEntity != null) {
      taskEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), task);
      taskEntity.setRevision(revisionLock.getLatestRevision());
    } else {
      taskEntity = new TaskEntity(task.getId(), revisionLock.getLatestRevision(), task.getLastModifiedByClient(),
          m_manager.find(ProjectEntity.class, task.getProjectId()), task.getName());

      taskEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), task);

      m_manager.persist(taskEntity);
    }

    final Task result = new Task();

    taskEntity.toDTO(result);

    return result;
  }

  private Query createQuery(final TaskFilter filter)
  {
    String combineStr = " where ";
    final StringBuffer queryStr = new StringBuffer("from " + TaskEntity.class.getName() + " t");

    if (filter.getActive() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.active = :active");
      combineStr = " and ";
    }
    if (filter.getDeleted() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.deleted = :deleted");
      combineStr = " and ";
    }
    if (filter.getProject() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.project.id = :projectId");
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

    switch (filter.getOrder()) {
      case ID:
        queryStr.append(" order by t.id asc");
        break;
      case NAME:
        queryStr.append(" order by t.name asc, t.id asc");
        break;
    }

    final Query query = m_manager.createQuery(queryStr.toString());

    if (filter.getActive() != null) {
      query.setParameter("active", filter.getActive());
    }
    if (filter.getDeleted() != null) {
      query.setParameter("deleted", filter.getDeleted());
    }
    if (filter.getProject() != null) {
      query.setParameter("projectId", filter.getProject());
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

    return query;
  }

}
