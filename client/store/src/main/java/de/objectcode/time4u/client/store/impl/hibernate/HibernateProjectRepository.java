package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.ProjectRepositoryEvent;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.context.SessionPersistenceContext;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

/**
 * Hibernate implementation of the project repository interface.
 * 
 * @author junglas
 * 
 */
public class HibernateProjectRepository implements IProjectRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateProjectRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public Project getProject(final String projectId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<Project>() {
      public Project perform(final Session session)
      {
        final ProjectEntity projectEntity = (ProjectEntity) session.get(ProjectEntity.class, projectId);

        if (projectEntity != null) {
          final Project project = new Project();
          projectEntity.toDTO(project);

          return project;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public ProjectSummary getProjectSummary(final String projectId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<ProjectSummary>() {
      public ProjectSummary perform(final Session session)
      {
        final ProjectEntity projectEntity = (ProjectEntity) session.get(ProjectEntity.class, projectId);

        if (projectEntity != null) {
          final ProjectSummary project = new ProjectSummary();
          projectEntity.toSummaryDTO(project);

          return project;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public List<Project> getProjects(final ProjectFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<Project>>() {
      public List<Project> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(ProjectEntity.class);

        if (filter.getActive() != null) {
          criteria.add(Restrictions.eq("active", filter.getActive()));
        }
        if (filter.getDeleted() != null) {
          criteria.add(Restrictions.eq("deleted", filter.getDeleted()));
        }
        if (filter.getParentProject() != null) {
          if (filter.getParentProject().equals("")) {
            criteria.add(Restrictions.isNull("parent"));
          } else {
            criteria.add(Restrictions.eq("parent.id", filter.getParentProject()));
          }
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        if (filter.getMaxRevision() != null) {
          criteria.add(Restrictions.le("revision", filter.getMaxRevision()));
        }
        if (filter.getLastModifiedByClient() != null) {
          criteria.add(Restrictions.eq("lastModifiedByClient", filter.getLastModifiedByClient()));
        }

        switch (filter.getOrder()) {
          case ID:
            criteria.addOrder(Order.asc("id"));
            break;
          case NAME:
            criteria.addOrder(Order.asc("name"));
            criteria.addOrder(Order.asc("id"));
            break;
        }

        final List<Project> result = new ArrayList<Project>();

        for (final Object row : criteria.list()) {
          final Project project = new Project();

          ((ProjectEntity) row).toDTO(project);

          result.add(project);
        }

        return result;
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  public List<ProjectSummary> getProjectSumaries(final ProjectFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<ProjectSummary>>() {
      public List<ProjectSummary> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(ProjectEntity.class);

        if (filter.getActive() != null) {
          criteria.add(Restrictions.eq("active", filter.getActive()));
        }
        if (filter.getDeleted() != null) {
          criteria.add(Restrictions.eq("deleted", filter.getDeleted()));
        }
        if (filter.getParentProject() != null) {
          if (filter.getParentProject().equals("")) {
            criteria.add(Restrictions.isNull("parent"));
          } else {
            criteria.add(Restrictions.eq("parent.id", filter.getParentProject()));
          }
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        if (filter.getMaxRevision() != null) {
          criteria.add(Restrictions.lt("revision", filter.getMaxRevision()));
        }
        if (filter.getLastModifiedByClient() != null) {
          criteria.add(Restrictions.eq("lastModifiedByClient", filter.getLastModifiedByClient()));
        }
        switch (filter.getOrder()) {
          case ID:
            criteria.addOrder(Order.asc("id"));
            break;
          case NAME:
            criteria.addOrder(Order.asc("name"));
            criteria.addOrder(Order.asc("id"));
            break;
        }

        final List<ProjectSummary> result = new ArrayList<ProjectSummary>();

        for (final Object row : criteria.list()) {
          final ProjectSummary project = new ProjectSummary();

          ((ProjectEntity) row).toSummaryDTO(project);

          result.add(project);
        }

        return result;
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  public void storeProject(final Project project, final boolean modifiedByOwner) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation() {
      public void perform(final Session session)
      {
        final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);
        final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.PROJECT, null);

        if (project.getId() == null) {
          project.setId(m_repository.generateLocalId(EntityType.PROJECT));
        }
        final ProjectEntity projectEntity = new ProjectEntity(project.getId(), revisionLock.getLatestRevision(),
            m_repository.getClientId(), project.getName());

        projectEntity.fromDTO(new SessionPersistenceContext(session), project);
        if (modifiedByOwner) {
          projectEntity.setLastModifiedByClient(m_repository.getClientId());
        }

        session.merge(projectEntity);
        session.flush();

        projectEntity.toDTO(project);
      }
    });

    m_repository.fireRepositoryEvent(new ProjectRepositoryEvent(project));
  }

  /**
   * {@inheritDoc}
   */
  public void deleteProject(final Project project) throws RepositoryException
  {
    final Project result = m_hibernateTemplate
        .executeInTransaction(new HibernateTemplate.OperationWithResult<Project>() {
          public Project perform(final Session session)
          {
            final ProjectEntity projectEntity = (ProjectEntity) session.get(ProjectEntity.class, project.getId());

            if (projectEntity == null) {
              return null;
            }

            final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);
            final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.PROJECT, null);

            projectEntity.setDeleted(true);
            projectEntity.setRevision(revisionLock.getLatestRevision());
            projectEntity.setLastModifiedByClient(m_repository.getClientId());

            session.flush();

            final Project result = new Project();

            projectEntity.toDTO(result);

            return result;
          }
        });

    if (result != null) {
      m_repository.fireRepositoryEvent(new ProjectRepositoryEvent(result));
    }
  }

  public List<ProjectSummary> getProjectPath(final String projectId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<ProjectSummary>>() {
      public List<ProjectSummary> perform(final Session session)
      {
        final List<ProjectSummary> result = new ArrayList<ProjectSummary>();
        ProjectEntity projectEntity = (ProjectEntity) session.get(ProjectEntity.class, projectId);

        while (projectEntity != null) {
          final ProjectSummary project = new ProjectSummary();

          projectEntity.toSummaryDTO(project);
          result.add(0, project);
          projectEntity = projectEntity.getParent();
        }

        return result;
      }
    });
  }
}
