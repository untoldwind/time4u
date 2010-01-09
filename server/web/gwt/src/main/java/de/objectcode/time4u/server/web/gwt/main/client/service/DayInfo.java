package de.objectcode.time4u.server.web.gwt.main.client.service;

import java.util.Date;
import java.util.List;

public class DayInfo extends DayInfoSummary {
	List<WorkItem> workItems;

	public DayInfo() {
		super();
	}

	public DayInfo(String id, Date day, boolean hasWorkItems,
			boolean hasInvalidWorkItems, boolean hasTags, int regularTime,
			List<WorkItem> workItems) {
		super(id, day, hasWorkItems, hasInvalidWorkItems, hasTags, regularTime);

		this.workItems = workItems;
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

}