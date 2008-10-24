package de.objectcode.time4u.server.web.rest.secure;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IWorkItemService;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;

public class DayInfos
{
  private final static Log LOG = LogFactory.getLog(DayInfos.class);

  private final IWorkItemService m_workItemService;

  public DayInfos()
  {
    try {
      final InitialContext ctx = new InitialContext();

      m_workItemService = (IWorkItemService) ctx.lookup("time4u-server/WorkItemService/remote");
    } catch (final Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException("Inizialize failed", e);
    }
  }

  @GET
  @Path("/")
  @Produces("text/xml")
  public FilterResult<? extends DayInfoSummary> getProjectSummaries(@QueryParam("minRevision") final Long minRevision,
      @QueryParam("maxRevision") final Long maxRevision, @QueryParam("full") final boolean full)
  {
    final DayInfoFilter filter = new DayInfoFilter();

    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    if (full) {
      return m_workItemService.getDayInfos(filter);
    }
    return m_workItemService.getDayInfoSummaries(filter);
  }
}
