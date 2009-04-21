package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.ITeamService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.filter.TeamFilter;

@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.ITeamService")
@SOAPBinding(style = Style.RPC)
public class TeamServiceWS implements ITeamService
{
  private static final Log LOG = LogFactory.getLog(ProjectServiceWS.class);

  private final ITeamService m_teamService;

  public TeamServiceWS() throws Exception
  {
    final InitialContext ctx = new InitialContext();

    m_teamService = (ITeamService) ctx.lookup("time4u-server/TeamService/remote");
  }

  public FilterResult<Team> getTeams(final TeamFilter filter)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getTeams: " + filter);
    }

    return m_teamService.getTeams(filter);
  }

  public FilterResult<TeamSummary> getTeamSummaries(final TeamFilter filter)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getTeamSummaries: " + filter);
    }

    return m_teamService.getTeamSummaries(filter);
  }

  public Team getTeam(final String teamId)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getTeam: " + teamId);
    }

    return m_teamService.getTeam(teamId);
  }

}
