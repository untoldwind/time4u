package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.Task;

public class TaskResource
{
  private final static Log LOG = LogFactory.getLog(TaskResource.class);

  private final Task m_task;
  private final IProjectService m_projectService;

  public TaskResource(final Task task)
  {
    m_task = task;
    try {
      final InitialContext ctx = new InitialContext();

      m_projectService = (IProjectService) ctx.lookup("time4u-server/ProjectService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public Task getTask()
  {
    return m_task;
  }

  @Path("/project")
  public ProjectResource getProject()
  {
    final Project project = m_projectService.getProject(m_task.getProjectId());

    if (project == null) {
      return null;
    }

    return new ProjectResource(project);
  }

}
