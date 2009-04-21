package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.filter.TeamFilter;

/**
 * Remote team service interface.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
public interface ITeamService
{
  /**
   * Get all teams matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  FilterResult<Team> getTeams(TeamFilter filter);

  /**
   * Get all team summaries matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  FilterResult<TeamSummary> getTeamSummaries(TeamFilter filter);

  /**
   * Get a team by its identifier.
   * 
   * @param teamId
   *          The identifier of the team.
   * 
   * @return The the <tt>Team</tt> with identifier <tt>teamId</tt>
   */
  @WebMethod
  Team getTeam(String teamId);
}
