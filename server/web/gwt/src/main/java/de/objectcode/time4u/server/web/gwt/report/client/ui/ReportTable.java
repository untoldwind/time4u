package de.objectcode.time4u.server.web.gwt.report.client.ui;

import java.util.List;

import de.objectcode.time4u.server.web.gwt.report.client.service.ReportColumnDefinition;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportRowData;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportTableData;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedFlexTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TableHeader;

public class ReportTable extends ExtendedFlexTable {
	List<ReportColumnDefinition> columns;
	List<ReportColumnDefinition> groupByColumns;

	public ReportTable() {
		setStyleName("utils-dataTable");
	}

	public void setData(ReportTableData report) {
		removeAllRows();

		columns = report.getColumns();
		groupByColumns = report.getGroupByColumns();

		TableHeader headers[] = new TableHeader[columns.size()
				+ groupByColumns.size()];

		int columnWidth = 100 / (headers.length > 0 ? headers.length : 1);

		for (int i = 0; i < groupByColumns.size(); i++) {
			headers[i] = new TableHeader(groupByColumns.get(i).getHeader(),
					columnWidth + "%");
		}
		for (int i = 0; i < columns.size(); i++) {
			headers[i + groupByColumns.size()] = new TableHeader(columns.get(i)
					.getHeader(), columnWidth + "%");
		}

		setHeaders(headers);
		setHeaderStyleName("utils-dataTable-header");
		
		int count = 0;
		for (ReportRowData row : report.getRows()) {
			String[] data  = row.getData();
			
			for (int i = 0; i < columns.size(); i++) {
				String value=columns.get(i).getColumnType().formatValue(data[i]);
				
				setText(count, i, value);
			}
			getRowFormatter().setStyleName(count, "utils-dataTable-row");
			getRowFormatter().addStyleName(
					count,
					count % 2 == 0 ? "utils-dataTable-row-even"
							: "utils-dataTable-row-odd");
			count++;
		}
	}
}
