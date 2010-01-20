package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.objectcode.time4u.server.web.gwt.utils.client.event.ColumnSortHandler;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageHandler;
import de.objectcode.time4u.server.web.gwt.utils.client.event.HasColumnSortHandlers;
import de.objectcode.time4u.server.web.gwt.utils.client.event.HasDataPageHandlers;
import de.objectcode.time4u.server.web.gwt.utils.client.service.IDataPage;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.ColumnSorting;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.DataPager;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.DataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.DataTableColumn;

public class PagedDataTable<RowClass> extends Composite implements
		HasDataPageHandlers, HasSelectionHandlers<RowClass>,
		HasColumnSortHandlers<RowClass> {

	private DataTable<RowClass> dataTable;
	private DataPager dataPager;
	private int pageSize;

	public PagedDataTable(int pageSize, DataTableColumn<RowClass>... columns) {
		this.pageSize = pageSize;
		dataTable = new DataTable<RowClass>(columns);
		dataTable.setFixedRowCount(pageSize);
		dataPager = new DataPager();

		VerticalPanel panel = new VerticalPanel();

		panel.add(dataTable);
		panel.add(dataPager);

		initWidget(panel);
	}

	public int getCurrentPage() {
		return dataPager.getCurrentPage();
	}

	public void setDataPage(IDataPage<RowClass> dataPage) {
		for (int i = 0; i < pageSize; i++) {
			dataTable.setRow(i, i < dataPage.getPageData().size() ? dataPage
					.getPageData().get(i) : null);
		}
		dataTable.updateSelection();

		dataPager.setDataPage(dataPage);
	}

	public HandlerRegistration addDataPageHandler(DataPageHandler handler) {
		return dataPager.addDataPageHandler(handler);
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<RowClass> handler) {
		return dataTable.addSelectionHandler(handler);
	}

	public HandlerRegistration addColumnSortHandler(
			ColumnSortHandler<RowClass> handler) {
		return dataTable.addColumnSortHandler(handler);
	}

	public void setColumnSorting(int columnIndex, ColumnSorting columnSorting,
			boolean fireEvent) {
		dataTable.setColumnSorting(columnIndex, columnSorting, fireEvent);
	}
}
