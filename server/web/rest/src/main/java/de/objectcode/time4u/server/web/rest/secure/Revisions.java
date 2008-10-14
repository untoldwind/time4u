package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.data.RevisionStatus;

public class Revisions
{
  private final static Log LOG = LogFactory.getLog(Revisions.class);

  private final IRevisionService m_revisionService;

  public Revisions()
  {
    try {
      final InitialContext ctx = new InitialContext();

      m_revisionService = (IRevisionService) ctx.lookup("time4u-server/RevisionService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public RevisionStatus getPingResult()
  {
    return m_revisionService.getRevisionStatus();
  }

}
