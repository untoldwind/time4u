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

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.filter.ProjectFilter;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.context.EntityManagerPersistenceContext;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

/**
 * EJB3 implementation of the project service interface.
 * 
 * @author junglas
 */
@Stateless
@Remote(IProjectService.class)
@RemoteBinding(jndiBinding = "time4u-server/ProjectService/remote")
@SecurityDomain("time4u")
public class ProjectServiceImpl implements IProjectService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private ILocalIdGenerator m_idGenerator;

  @Resource
  SessionContext m_sessionContext;

  public Project getProject(final String projectId)
  {
    final ProjectEntity projectEntity = m_manager.find(ProjectEntity.class, projectId);

    if (projectEntity != null) {
      final Project project = new Project();
      projectEntity.toDTO(project);

      return project;
    }
    return null;
  }

  public ProjectSummary getProjectSummary(final String projectId)
  {
    final ProjectEntity projectEntity = m_manager.find(ProjectEntity.class, projectId);

    if (projectEntity != null) {
      final ProjectSummary project = new ProjectSummary();
      projectEntity.toSummaryDTO(project);

      return project;
    }
    return null;
  }

  public FilterResult<Project> getProjects(final ProjectFilter filter)
  {
    final Query query = createQuery(filter);
    final List<Project> result = new ArrayList<Project>();

    for (final Object row : query.getResultList()) {
      final Project project = new Project();

      ((ProjectEntity) row).toDTO(project);

      result.add(project);
    }

    return new FilterResult<Project>(result);
  }

  public FilterResult<ProjectSummary> getProjectSumaries(final ProjectFilter filter)
  {
    final Query query = createQuery(filter);
    final List<ProjectSummary> result = new ArrayList<ProjectSummary>();

    for (final Object row : query.getResultList()) {
      final ProjectSummary project = new ProjectSummary();

      ((ProjectEntity) row).toSummaryDTO(project);

      result.add(project);
    }

    return new FilterResult<ProjectSummary>(result);
  }

  public Project storeProject(final Project project)
  {
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.PROJECT, null);

    ProjectEntity projectEntity = null;

    if (project.getId() != null) {
      projectEntity = m_manager.find(ProjectEntity.class, project.getId());
    } else {
      project.setId(m_idGenerator.generateLocalId(EntityType.PROJECT));
    }
    if (projectEntity != null) {
      projectEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), project);
      projectEntity.setRevision(revisionLock.getLatestRevision());
    } else {
      projectEntity = new ProjectEntity(project.getId(), revisionLock.getLatestRevision(), project
          .getLastModifiedByClient(), project.getName());

      projectEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), project);

      m_manager.persist(projectEntity);
    }

    final Project result = new Project();

    projectEntity.toDTO(result);

    return result;
  }

  private Query createQuery(final ProjectFilter filter)
  {
    String combineStr = " where ";
    final StringBuffer queryStr = new StringBuffer("from " + ProjectEntity.class.getName() + " p");

    if (filter.getActive() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.active = :active");
      combineStr = " and ";
    }
    if (filter.getDeleted() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.deleted = :deleted");
      combineStr = " and ";
    }
    if (filter.getParentProject() != null) {
      queryStr.append(combineStr);
      if (filter.getParentProject().equals("")) {
        queryStr.append("p.parent is null");
      } else {
        queryStr.append("p.parent.id = :parentId");
      }
      combineStr = " and ";
    }
    if (filter.getMinRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.revision >= :minRevision");
      combineStr = " and ";
    }
    if (filter.getMaxRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.revision <= :maxRevision");
      combineStr = " and ";
    }
    if (filter.getLastModifiedByClient() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.lastModifiedByClient = :lastModifiedByClient");
      combineStr = " and ";
    }

    switch (filter.getOrder()) {
      case ID:
        queryStr.append(" order by p.id asc");
        break;
      case NAME:
        queryStr.append(" order by p.name asc, p.id asc");
        break;
    }

    final Query query = m_manager.createQuery(queryStr.toString());

    if (filter.getActive() != null) {
      query.setParameter("active", filter.getActive());
    }
    if (filter.getDeleted() != null) {
      query.setParameter("deleted", filter.getDeleted());
    }
    if (filter.getParentProject() != null) {
      if (!filter.getParentProject().equals("")) {
        query.setParameter("parentId", filter.getParentProject());
      }
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
