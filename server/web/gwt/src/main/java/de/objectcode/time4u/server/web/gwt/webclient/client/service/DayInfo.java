package de.objectcode.time4u.server.web.gwt.webclient.client.service;

import java.util.Date;
import java.util.List;

public class DayInfo extends DayInfoSummary {
	List<WorkItem> workItems;

	public DayInfo() {
		super();
	}

	public DayInfo(String id, Date day, boolean hasWorkItems,
			boolean hasInvalidWorkItems, boolean hasTags, int regularTime, int calculatedRegularTime,
			List<WorkItem> workItems) {
		super(id, day, hasWorkItems, hasInvalidWorkItems, hasTags, regularTime, calculatedRegularTime);

		this.workItems = workItems;
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

}
