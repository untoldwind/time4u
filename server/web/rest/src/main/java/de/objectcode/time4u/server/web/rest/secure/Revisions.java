package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.data.RevisionStatus;

@Path("/secure/revisions")
public class Revisions
{
  private final IRevisionService m_revisionService;

  public Revisions() throws Exception
  {
    final InitialContext ctx = new InitialContext();

    m_revisionService = (IRevisionService) ctx.lookup("time4u-server/RevisionService/remote");
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public RevisionStatus getPingResult()
  {
    return m_revisionService.getRevisionStatus();
  }

}
