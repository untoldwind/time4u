package de.objectcode.time4u.server.web.gwt.webclient.client.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("workItem.service")
public interface WorkItemService extends RemoteService {
	List<DayInfoSummary> getDayInfoSummaries(Date start, Date end);

	DayInfo getDayInfo(Date day);
}
