package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.DayTag;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.TimePolicy;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;
import de.objectcode.time4u.server.api.filter.TimePolicyFilter;

/**
 * Remote workitem service interface.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
public interface IWorkItemService
{
  @WebMethod
  DayInfo getDayInfo(final CalendarDay day);

  @WebMethod
  FilterResult<DayInfo> getDayInfos(DayInfoFilter filter);

  @WebMethod
  FilterResult<DayInfoSummary> getDayInfoSummaries(DayInfoFilter filter);

  @WebMethod
  DayInfo storeDayInfo(DayInfo dayInfo);

  @WebMethod
  FilterResult<TimePolicy> getTimePolicies(TimePolicyFilter filter);

  @WebMethod
  TimePolicy storeTimePolicy(TimePolicy timePolicy);

  @WebMethod
  FilterResult<DayTag> getDayTags();
}
