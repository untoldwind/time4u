package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

@Path("/secure/projects")
public class Projects
{
  private final IProjectService m_projectService;

  public Projects() throws Exception
  {
    final InitialContext ctx = new InitialContext();

    m_projectService = (IProjectService) ctx.lookup("time4u-server/ProjectService/remote");
  }

  @GET
  @Path("/summaries")
  @Produces("text/xml")
  public FilterResult<ProjectSummary> getProjectSummaries(@MatrixParam("active") final Boolean active,
      @MatrixParam("deleted") final Boolean deleted, @MatrixParam("minRevision") final Long minRevision,
      @MatrixParam("maxRevision") final Long maxRevision)
  {
    final ProjectFilter filter = new ProjectFilter();
    filter.setActive(active);
    filter.setDeleted(deleted);
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    System.out.println(">" + filter);
    return m_projectService.getProjectSumaries(filter);
  }

  @GET
  @Path("/summary/{id}")
  @Produces("text/xml")
  public ProjectSummary getProjectSummary(@PathParam("id") final String projectId)
  {
    return m_projectService.getProjectSummary(projectId);
  }

  @GET
  @Path("/full/{id}")
  @Produces("text/xml")
  public Project getProject(@PathParam("id") final String projctId)
  {
    return m_projectService.getProject(projctId);
  }

}
