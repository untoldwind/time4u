package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.List;

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

import de.objectcode.time4u.server.ejb.seam.api.IProjectServiceLocal;
import de.objectcode.time4u.server.entities.ProjectEntity;

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
}
