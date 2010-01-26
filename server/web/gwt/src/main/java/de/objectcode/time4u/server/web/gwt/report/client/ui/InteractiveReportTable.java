package de.objectcode.time4u.server.web.gwt.report.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableData;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableColumnType;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRow;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRowType;
import de.objectcode.time4u.server.web.gwt.report.client.service.IdLabelPair;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedFlexTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TableHeader;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TimeFormat;

public class InteractiveReportTable extends ExtendedFlexTable {
	Callback callback;
	CrossTableColumnType columnType;
	CrossTableRowType rowType;
	IdLabelPair[] columnHeaders;
	IdLabelPair[] rowHeaders;
	
	public InteractiveReportTable() {
		setStyleName("utils-dataTable");

		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if ( callback == null )
					return;
				
				Cell cell = getCellForEvent(event);
				if (cell != null) {
					int rowNum = cell.getRowIndex();
					int colNum = cell.getCellIndex();
					
					if ( rowNum == -1 ) {
						if ( colNum > 0 && colNum <= columnHeaders.length )
							callback.onColumnHeaderClick(columnType, columnHeaders[colNum -1]);
					} else if ( rowNum >= 0 && rowNum < rowHeaders.length ) {
						if ( colNum == 0 )
							callback.onRowHeaderClick(rowType, rowHeaders[rowNum]);
					}
				}				
			}
		});
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public void setData(CrossTableData crossTable) {
		removeAllRows();

		columnType = crossTable.getColumnType();
		rowType = crossTable.getRowType();
		
		columnHeaders = crossTable.getColumnHeaders();
		
		int columnCount = columnHeaders.length;
		int rowCount = crossTable.getRows().length;

		TableHeader headers[] = new TableHeader[columnCount + 2];

		headers[0] = new TableHeader("", "20%");
		headers[columnCount + 1] = new TableHeader("", "10%");

		int columnWidth = 70 / (columnCount > 0 ? columnCount : 1);

		for (int i = 0; i < crossTable.getColumnHeaders().length; i++)
			headers[i + 1] = new TableHeader(crossTable.getColumnHeaders()[i]
					.getLabel(), columnWidth + "%");

		setHeaders(headers);
		setHeaderStyleName("utils-dataTable-header");

		rowHeaders = new IdLabelPair[rowCount];
		
		for (int i = 0; i < rowCount; i++) {
			CrossTableRow row = crossTable.getRows()[i];

			rowHeaders[i] = row.getRowHeader();
			
			setText(i, 0, row.getRowHeader().getLabel());
			setText(i, columnCount + 1, TimeFormat
					.format(row.getRowAggregate()));

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

		setText(rowCount, headers.length - 1, TimeFormat.format(crossTable
				.getTotalAggregate()));

		for (int j = 0; j < columnCount; j++) {
			setText(crossTable.getRows().length, j + 1, String
					.valueOf(TimeFormat
							.format(crossTable.getColumnAggregates()[j])));
		}
		getRowFormatter().setStyleName(crossTable.getRows().length,
				"utils-dataTable-row");
		getRowFormatter().addStyleName(crossTable.getRows().length,
				"utils-dataTable-footer");
	}

	public static interface Callback {

		void onColumnHeaderClick(CrossTableColumnType columnType,
				IdLabelPair idValuePair);

		void onRowHeaderClick(CrossTableRowType rowType, IdLabelPair idValuePair);
		
	}
}
