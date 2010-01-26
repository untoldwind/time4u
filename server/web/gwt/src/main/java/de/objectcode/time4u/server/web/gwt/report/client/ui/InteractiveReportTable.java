package de.objectcode.time4u.server.web.gwt.report.client.ui;

import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTable;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRow;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedFlexTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TableHeader;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TimeFormat;

public class InteractiveReportTable extends ExtendedFlexTable {
	public InteractiveReportTable() {
		setStyleName("utils-dataTable");
	}

	public void setData(CrossTable crossTable) {
		removeAllRows();

		int columnCount = crossTable.getColumnHeaders().length;
		int rowCount = crossTable.getRows().length;
		
		TableHeader headers[] = new TableHeader[columnCount + 2];

		headers[0] = new TableHeader("", "20%");
		headers[columnCount + 1] = new TableHeader("", "10%");

		int columnWidth = 70 / columnCount;

		for (int i = 0; i < crossTable.getColumnHeaders().length; i++)
			headers[i + 1] = new TableHeader(crossTable.getColumnHeaders()[i]
					.getLabel(), columnWidth + "%");

		setHeaders(headers);
		setHeaderStyleName("utils-dataTable-header");

		for (int i = 0; i < rowCount; i++) {
			CrossTableRow row = crossTable.getRows()[i];

			setText(i, 0, row.getRowHeader().getLabel());
			setText(i, columnCount + 1, TimeFormat.format(row.getRowAggregate()));
			
			for (int j = 0; j < columnCount; j++) {
				setText(i, j + 1, String.valueOf(TimeFormat.format(row
						.getData()[j])));
			}
			getRowFormatter().setStyleName(i, "utils-dataTable-row");
			getRowFormatter().addStyleName(
					i,
					i % 2 == 0 ? "utils-dataTable-row-even"
							: "utils-dataTable-row-odd");
		}
		
		setText(rowCount, headers.length -1, TimeFormat.format(crossTable.getTotalAggregate()));

		for (int j = 0; j < columnCount; j++) {
			setText(crossTable.getRows().length, j + 1, String.valueOf(TimeFormat.format(crossTable.getColumnAggregates()[j])));
		}
		getRowFormatter().setStyleName(crossTable.getRows().length, "utils-dataTable-row");
		getRowFormatter().addStyleName(crossTable.getRows().length, "utils-dataTable-footer");
		
	}
}
