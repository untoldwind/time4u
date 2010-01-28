package de.objectcode.time4u.server.web.gwt.report.client.ui;

import de.objectcode.time4u.server.web.gwt.report.client.service.ReportColumnDefinition;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportColumnType;
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


		int headerWeights = 0;

		for (int i = 0; i < groupByColumns.length; i++) {
			if (groupByColumns[i].getColumnType() == ReportColumnType.DESCRIPTION
					|| groupByColumns[i].getColumnType() == ReportColumnType.NAME_ARRAY)
				headerWeights += 2;
			else
				headerWeights++;
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnType() == ReportColumnType.DESCRIPTION
					|| columns[i].getColumnType() == ReportColumnType.NAME_ARRAY)
				headerWeights += 2;
			else
				headerWeights++;
		}
		int columnWidth = 100 / (headerWeights > 0 ? headerWeights : 1);

		TableHeader headers[] = new TableHeader[columns.length
		                        				+ groupByColumns.length];

		for (int i = 0; i < groupByColumns.length; i++) {
			if (groupByColumns[i].getColumnType() == ReportColumnType.DESCRIPTION
					|| groupByColumns[i].getColumnType() == ReportColumnType.NAME_ARRAY)
				headers[i] = new TableHeader(groupByColumns[i].getHeader(), (2*columnWidth) + "%");
			else
				headers[i] = new TableHeader(groupByColumns[i].getHeader(), columnWidth + "%");
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnType() == ReportColumnType.DESCRIPTION
					|| columns[i].getColumnType() == ReportColumnType.NAME_ARRAY)
				headers[i + groupByColumns.length] = new TableHeader(columns[i]
				                                         					.getHeader(), (2*columnWidth) + "%");
			else
				headers[i + groupByColumns.length] = new TableHeader(columns[i]
				                                         					.getHeader(), columnWidth + "%");
		}

		setHeaders(headers);
		setHeaderStyleName("utils-dataTable-header");

		String[] rowData = report.getRows();
		int count = 0;
		int rowCount = 0;
		while (count < rowData.length) {
			for (int i = 0; i < columns.length; i++) {
				String value = columns[i].getColumnType().formatValue(
						rowData[count++]);

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
