package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import java.util.Collection;

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

public class PagedDataTable<RowClass> extends Composite implements
		HasDataPageHandlers, HasSelectionHandlers<RowClass>,
		HasColumnSortHandlers<RowClass>,IDataView<RowClass> {

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

	public void setData(Collection<RowClass> data) {
		dataTable.setData(data);
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

	public int getCurrentSortingIndex() {
		return dataTable.getCurrentSortingIndex();
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		
		dataTable.setWidth(width);
	}

	public boolean isCurrentSortingAscending() {
		return dataTable.isCurrentSortingAscending();
	}

	public DataTableColumn<RowClass> getCurrentSortingColumn() {
		return dataTable.getCurrentSortingColumn();
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

	public void setColumnSorting(int columnIndex, boolean ascending,
			boolean fireEvent) {
		dataTable.setColumnSorting(columnIndex, ascending, fireEvent);

	}
}
