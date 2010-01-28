package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CrossTableRow implements IsSerializable {
	IdLabelPair rowHeader;
	int[] data;
	int rowAggregate;

	public CrossTableRow() {
	}

	public CrossTableRow(IdLabelPair rowHeader, int[] data, int rowAggregate) {
		this.rowHeader = rowHeader;
		this.data = data;
		this.rowAggregate = rowAggregate;
	}

	public IdLabelPair getRowHeader() {
		return rowHeader;
	}

	public int[] getData() {
		return data;
	}

	public int getRowAggregate() {
		return rowAggregate;
	}

}
