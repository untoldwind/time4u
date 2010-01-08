package de.objectcode.time4u.server.web.gwt.main.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DayInfoSummary implements IsSerializable {
	private String id;
	private Date day;
	private boolean hasWorkItems;
	private boolean hasInvalidWorkItems;
	private boolean hasTags;
	private int regularTime;

	public DayInfoSummary() {
		
	}
	
	public DayInfoSummary(String id, Date day, boolean hasWorkItems,
			boolean hasInvalidWorkItems, boolean hasTags, int regularTime) {
		this.id = id;
		this.day = day;
		this.hasWorkItems = hasWorkItems;
		this.hasInvalidWorkItems = hasInvalidWorkItems;
		this.hasTags = hasTags;
		this.regularTime = regularTime;
	}

	public String getId() {
		return id;
	}

	public Date getDay() {
		return day;
	}

	public boolean isHasWorkItems() {
		return hasWorkItems;
	}

	public boolean isHasInvalidWorkItems() {
		return hasInvalidWorkItems;
	}

	public boolean isHasTags() {
		return hasTags;
	}

	public int getRegularTime() {
		return regularTime;
	}

}
