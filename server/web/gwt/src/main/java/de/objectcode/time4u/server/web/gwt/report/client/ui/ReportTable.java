package de.objectcode.time4u.server.web.gwt.report.client.ui;

import de.objectcode.time4u.server.web.gwt.report.client.service.ReportColumnDefinition;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportTableData;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedFlexTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TableHeader;

public class ReportTable extends ExtendedFlexTable {
	ReportColumnDefinition[] columns;
	ReportColumnDefinition[] groupByColumns;

	public ReportTable() {
		setStyleName("utils-dataTable");
	}

	public void setData(ReportTableData report) {
		removeAllRows();

		columns = report.getColumns();
		groupByColumns = report.getGroupByColumns();

		TableHeader headers[] = new TableHeader[columns.length
				+ groupByColumns.length];

		int columnWidth = 100 / (headers.length > 0 ? headers.length : 1);

		for (int i = 0; i < groupByColumns.length; i++) {
			headers[i] = new TableHeader(groupByColumns[i].getHeader(),
					columnWidth + "%");
		}
		for (int i = 0; i < columns.length; i++) {
			headers[i + groupByColumns.length] = new TableHeader(columns[i]
					.getHeader(), columnWidth + "%");
		}

		setHeaders(headers);
		setHeaderStyleName("utils-dataTable-header");
		
		String[] rowData = report.getRows();
		int count = 0;
		int rowCount = 0;
		while ( count < rowData.length ) {
			for (int i = 0; i < columns.length; i++) {
				String value=columns[i].getColumnType().formatValue(rowData[count++]);
				
				setText(rowCount, i, value);
			}
			getRowFormatter().setStyleName(rowCount, "utils-dataTable-row");
			getRowFormatter().addStyleName(
					rowCount,
					rowCount % 2 == 0 ? "utils-dataTable-row-even"
							: "utils-dataTable-row-odd");
			rowCount++;
		}
	}
}
