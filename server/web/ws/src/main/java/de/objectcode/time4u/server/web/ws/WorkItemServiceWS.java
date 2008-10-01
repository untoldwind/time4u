package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.IWorkItemService;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;

@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.IWorkItemService")
@SOAPBinding(style = Style.RPC)
public class WorkItemServiceWS implements IWorkItemService
{
  private static final Log LOG = LogFactory.getLog(WorkItemServiceWS.class);

  private final IWorkItemService m_workItemService;

  public WorkItemServiceWS() throws Exception
  {
    final InitialContext ctx = new InitialContext();

    m_workItemService = (IWorkItemService) ctx.lookup("time4u-server/WorkItemService/remote");
  }

  public DayInfo getDayInfo(final CalendarDay day)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getDayInfo: " + day);
    }
    return m_workItemService.getDayInfo(day);
  }

  public FilterResult<DayInfo> getDayInfos(final DayInfoFilter filter)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getDayInfo: " + filter);
    }
    return m_workItemService.getDayInfos(filter);
  }

  public FilterResult<DayInfoSummary> getDayInfoSummaries(final DayInfoFilter filter)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getDayInfoSummaries: " + filter);
    }
    return m_workItemService.getDayInfoSummaries(filter);
  }

  public DayInfo storeDayInfo(final DayInfo dayInfo)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("storeDayInfo: " + dayInfo);
    }
    return m_workItemService.storeDayInfo(dayInfo);
  }
}
