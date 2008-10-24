package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

/**
 * Web-service delegate to the project service implementation.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.IProjectService")
@SOAPBinding(style = Style.RPC)
public class ProjectServiceWS implements IProjectService
{
  private static final Log LOG = LogFactory.getLog(ProjectServiceWS.class);

  private final IProjectService m_projectService;

  public ProjectServiceWS() throws Exception
  {
    final InitialContext ctx = new InitialContext();

    m_projectService = (IProjectService) ctx.lookup("time4u-server/ProjectService/remote");
  }

  public Project getProject(final String projectId)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getProject: " + projectId);
    }

    return m_projectService.getProject(projectId);
  }

  public ProjectSummary getProjectSummary(final String projectId)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getProjectSummary: " + projectId);
    }

    return m_projectService.getProjectSummary(projectId);
  }

  public FilterResult<Project> getProjects(final ProjectFilter filter)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getProjects: " + filter);
    }

    return m_projectService.getProjects(filter);
  }

  public FilterResult<ProjectSummary> getProjectSumaries(final ProjectFilter filter)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getProjectSummaries: " + filter);
    }
    return m_projectService.getProjectSumaries(filter);
  }

  public Project storeProject(final Project project)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("storeProject: " + project);
    }

    return m_projectService.storeProject(project);
  }
}
