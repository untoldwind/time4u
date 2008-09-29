package de.objectcode.time4u.server.web.ws;

import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.ServiceException;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

@WebService
@SOAPBinding(style = Style.RPC)
public class ProjectServiceWS implements IProjectService
{
  @WebMethod
  public Project getProject(final UUID projectId) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @WebMethod
  public FilterResult<Project> getProjects(final ProjectFilter filter) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @WebMethod
  public FilterResult<ProjectSummary> getProjectSumaries(final ProjectFilter filter) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @WebMethod
  public ProjectSummary getProjectSummary(final UUID projectId) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @WebMethod
  public Project storeProject(final Project project) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

}
