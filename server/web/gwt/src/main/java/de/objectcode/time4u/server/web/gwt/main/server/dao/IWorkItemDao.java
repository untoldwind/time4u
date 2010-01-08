package de.objectcode.time4u.server.web.gwt.main.server.dao;

import java.util.Date;
import java.util.List;

import de.objectcode.time4u.server.web.gwt.main.client.service.DayInfoSummary;

public interface IWorkItemDao {
	List<DayInfoSummary> findDayInfoDTOSummary(String personId, Date start, Date end);
}
