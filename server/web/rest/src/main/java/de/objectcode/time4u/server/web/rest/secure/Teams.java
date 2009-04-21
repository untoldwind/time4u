package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.ITeamService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.filter.TeamFilter;

public class Teams
{
  private final static Log LOG = LogFactory.getLog(Tasks.class);

  private ITeamService m_teamService;

  public Teams()
  {
    try {
      final InitialContext ctx = new InitialContext();

      m_teamService = (ITeamService) ctx.lookup("time4u-server/TeamService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public FilterResult<? extends TeamSummary> getProjectSummaries(@QueryParam("deleted") final Boolean deleted,
      @QueryParam("minRevision") final Long minRevision, @QueryParam("maxRevision") final Long maxRevision,
      @QueryParam("full") final boolean full)
  {
    final TeamFilter filter = new TeamFilter();
    filter.setDeleted(deleted);
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    if (full) {
      return m_teamService.getTeams(filter);
    }
    return m_teamService.getTeamSummaries(filter);
  }

  @Path("/{id}")
  public TeamResource getTeam(@PathParam("id") final String teamId)
  {
    final Team team = m_teamService.getTeam(teamId);

    if (team == null) {
      return null;
    }
    return new TeamResource(team);
  }
}
