package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CrossTableData implements IsSerializable {
	CrossTableColumnType columnType;
	CrossTableRowType rowType;
	IdLabelPair[] columnHeaders;
	CrossTableRow[] rows;
	int[] columnAggregates;
	int totalAggregate;

	public CrossTableData() {
	}

	public CrossTableData(CrossTableColumnType columnType,
			CrossTableRowType rowType, IdLabelPair[] columnHeaders,
			CrossTableRow[] rows, int[] columnAggregates, int totalAggregate) {
		this.columnType = columnType;
		this.rowType = rowType;
		this.columnHeaders = columnHeaders;
		this.rows = rows;
		this.columnAggregates = columnAggregates;
		this.totalAggregate = totalAggregate;
	}

	public IdLabelPair[] getColumnHeaders() {
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

	public CrossTableColumnType getColumnType() {
		return columnType;
	}

	public CrossTableRowType getRowType() {
		return rowType;
	}

}
