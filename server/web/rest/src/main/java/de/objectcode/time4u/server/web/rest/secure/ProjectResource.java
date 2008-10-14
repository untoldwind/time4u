package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;
import de.objectcode.time4u.server.api.filter.TaskFilter;

public class ProjectResource
{
  private final static Log LOG = LogFactory.getLog(ProjectResource.class);

  private final Project m_project;
  private final IProjectService m_projectService;
  private final ITaskService m_taskService;

  public ProjectResource(final Project project)
  {
    m_project = project;
    try {
      final InitialContext ctx = new InitialContext();

      m_projectService = (IProjectService) ctx.lookup("time4u-server/ProjectService/remote");
      m_taskService = (ITaskService) ctx.lookup("time4u-server/TaskService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public Project getProject()
  {
    return m_project;
  }

  @Path("/parent")
  public ProjectResource getParent()
  {
    if (m_project.getParentId() == null) {
      return null;
    }

    final Project project = m_projectService.getProject(m_project.getParentId());

    if (project == null) {
      return null;
    }

    return new ProjectResource(project);
  }

  @GET
  @Path("/children")
  @Produces("text/xml")
  public FilterResult<ProjectSummary> getChildren()
  {
    return m_projectService.getProjectSumaries(ProjectFilter.filterChildProjects(m_project.getId(), false));
  }

  @GET
  @Path("/tasks")
  @Produces("text/xml")
  public FilterResult<TaskSummary> getTasks()
  {
    return m_taskService.getTaskSummaries(TaskFilter.filterProjectTasks(m_project.getId(), false));
  }

}
