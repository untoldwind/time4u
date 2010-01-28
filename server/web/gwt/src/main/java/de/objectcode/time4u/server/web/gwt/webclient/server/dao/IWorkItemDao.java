package de.objectcode.time4u.server.web.gwt.webclient.server.dao;

import java.util.Date;
import java.util.List;

import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.DayInfo;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.DayInfoSummary;

public interface IWorkItemDao {	
	List<DayInfoSummary> findDayInfoDTOSummary(String personId, Date start, Date end);

	DayInfo findDayInfoDTO(String personId, Date day);
	
	List<TimePolicyEntity> findTimePolicies(String personId);
}
