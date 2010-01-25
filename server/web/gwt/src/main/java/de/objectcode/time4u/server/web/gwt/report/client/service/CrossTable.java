package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CrossTable implements IsSerializable {
	IdValuePair[] columnHeaders;
	CrossTableRow[] rows;
	int[] columnAggregates;
	int totalAggregate;

	public CrossTable() {
	}

	public CrossTable(IdValuePair[] columnHeaders, CrossTableRow[] rows,
			int[] columnAggregates, int totalAggregate) {
		this.columnHeaders = columnHeaders;
		this.rows = rows;
		this.columnAggregates = columnAggregates;
		this.totalAggregate = totalAggregate;
	}

	public IdValuePair[] getColumnHeaders() {
		return columnHeaders;
	}

	public CrossTableRow[] getRows() {
		return rows;
	}

	public int[] getColumnAggregates() {
		return columnAggregates;
	}

	public int getTotalAggregate() {
		return totalAggregate;
	}

}
