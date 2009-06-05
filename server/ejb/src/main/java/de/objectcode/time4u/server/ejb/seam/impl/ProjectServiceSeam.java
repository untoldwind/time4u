package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.IProjectServiceLocal;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

@Stateless
@Local(IProjectServiceLocal.class)
@org.jboss.annotation.ejb.LocalBinding(jndiBinding = "time4u-server/seam/ProjectServiceSeam/local")
@org.jboss.ejb3.annotation.LocalBinding(jndiBinding = "time4u-server/seam/ProjectServiceSeam/local")
@Name("ProjectService")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class ProjectServiceSeam implements IProjectServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private ILocalIdGenerator m_idGenerator;

  @Restrict("#{s:hasRole('user')}")
  public ProjectEntity getProject(final String projectId)
  {
    return m_manager.find(ProjectEntity.class, projectId);
  }

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('user')}")
  public List<ProjectEntity> getRootProjects()
  {
    final Query query = m_manager.createQuery("from " + ProjectEntity.class.getName()
        + " p where p.parent is null and p.deleted = false order by p.name");

    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('user')}")
  public List<ProjectEntity> getChildProjects(final String projectId)
  {
    if (projectId == null || projectId.length() == 0) {
      return getRootProjects();
    }
    final Query query = m_manager.createQuery("from " + ProjectEntity.class.getName()
        + " p where p.parent.id = :projectId and p.deleted = false order by p.name");

    query.setParameter("projectId", projectId);

    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('user')}")
  public List<TaskEntity> getTasks(final String projectId)
  {
    final Query query = m_manager.createQuery("from " + TaskEntity.class.getName()
        + " t where t.project.id = :projectId and t.deleted = false order by t.name");

    query.setParameter("projectId", projectId);

    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('admin')}")
  public Map<PersonEntity, Long> checkTransferData(final String fromTaskId)
  {
    final Query query = m_manager.createQuery("select w.dayInfo.person, count(*) from "
        + WorkItemEntity.class.getName() + " w where w.task.id = :taskId group by w.dayInfo.person.id");

    query.setParameter("taskId", fromTaskId);

    final List<Object[]> rows = query.getResultList();
    final Map<PersonEntity, Long> result = new HashMap<PersonEntity, Long>();

    for (final Object[] row : rows) {
      result.put((PersonEntity) row[0], (Long) row[1]);
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  @Restrict("#{s:hasRole('admin')}")
  public void transferData(final List<String> personIds, final String fromTaskId, final String toTaskId)
  {
    final TaskEntity fromTask = m_manager.find(TaskEntity.class, fromTaskId);
    final TaskEntity toTask = m_manager.find(TaskEntity.class, toTaskId);
    final ProjectEntity toProject = toTask.getProject();

    for (final String personId : personIds) {
      final PersonEntity person = m_manager.find(PersonEntity.class, personId);
      final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.DAYINFO, person.getId());

      final Query query = m_manager.createQuery("from " + WorkItemEntity.class.getName()
          + " w join fetch w.dayInfo where w.task.id = :taskId and w.dayInfo.person.id = :personId");

      query.setParameter("taskId", fromTask.getId());
      query.setParameter("personId", person.getId());

      final List<WorkItemEntity> result = query.getResultList();

      for (final WorkItemEntity workItem : result) {
        final DayInfoEntity dayInfo = workItem.getDayInfo();

        dayInfo.setLastModifiedByClient(m_idGenerator.getClientId());
        dayInfo.setRevision(revisionLock.getLatestRevision());
        workItem.setProject(toProject);
        workItem.setTask(toTask);
      }

      m_manager.flush();
      m_manager.clear();
    }
  }
}
