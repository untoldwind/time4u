package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReportRowData implements IsSerializable {
	/** The row index. */
	int index;
	/** The values of each column. */
	String[] data;

	public ReportRowData() {		
	}
	
	public ReportRowData(final int index, final String[] data) {
		this.index = index;
		this.data = data;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public String[] getData() {
		return data;
	}

	public void setData(final String[] data) {
		this.data = data;
	}

}
