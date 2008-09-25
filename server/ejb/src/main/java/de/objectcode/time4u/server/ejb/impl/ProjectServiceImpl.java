package de.objectcode.time4u.server.ejb.impl;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;

import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.ServiceException;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

/**
 * EJB3 implementation of the project service interface.
 * 
 * @author junglas
 */
@Stateless
@Remote(IProjectService.class)
@RemoteBinding(jndiBinding = "time4u-server/ProjectServiceBean/remote")
public class ProjectServiceImpl implements IProjectService
{

  public Project getProject(final long projectId) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Project> getProjects(final ProjectFilter filter) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public List<ProjectSummary> getProjectSumaries(final ProjectFilter filter) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Project storeProject(final Project project) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

}
