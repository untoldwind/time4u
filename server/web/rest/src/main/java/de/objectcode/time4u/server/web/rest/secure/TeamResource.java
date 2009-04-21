package de.objectcode.time4u.server.web.rest.secure;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.data.Team;

public class TeamResource
{
  private final static Log LOG = LogFactory.getLog(TeamResource.class);

  private final Team m_team;

  public TeamResource(final Team team)
  {
    m_team = team;
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public Team getTeam()
  {
    return m_team;
  }

}
