package de.objectcode.time4u.server.web.gwt.webclient.client.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.objectcode.time4u.server.web.gwt.webclient.client.service.DayInfoSummary;

public interface WorkItemServiceAsync {

	void getDayInfoSummaries(Date start, Date end,
			AsyncCallback<List<DayInfoSummary>> callback);

	void getDayInfo(Date day, AsyncCallback<DayInfo> callback);

}
