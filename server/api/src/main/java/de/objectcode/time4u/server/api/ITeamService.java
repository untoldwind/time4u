package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.Team;

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
   * Store a team. This method either inserts a new teams or updates an existing one.
   * 
   * @param team
   *          The team to be stored
   */
  @WebMethod
  Team storeTeam(Team team);
}
