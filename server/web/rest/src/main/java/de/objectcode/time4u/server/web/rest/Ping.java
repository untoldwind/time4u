package de.objectcode.time4u.server.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.objectcode.time4u.server.api.IConstants;
import de.objectcode.time4u.server.api.data.PingResult;

@Path("/ping")
public class Ping
{
  @GET
  @Path("/")
  @Produces("text/xml")
  public PingResult getPingResult()
  {
    final PingResult result = new PingResult();

    // Report server version of the API package
    result.setApiVersionMajor(IConstants.API_VERSION_MAJOR);
    result.setApiVersionMinor(IConstants.API_VERSION_MINOR);

    return result;
  }
}
