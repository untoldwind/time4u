package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.objectcode.time4u.server.web.gwt.utils.client.event.ColumnSortEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.event.ColumnSortHandler;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageHandler;
import de.objectcode.time4u.server.web.gwt.utils.client.event.HasColumnSortHandlers;
import de.objectcode.time4u.server.web.gwt.utils.client.event.HasDataPageHandlers;
import de.objectcode.time4u.server.web.gwt.utils.client.service.IDataPage;

public class PagedDataTable<RowClass> extends Composite implements
		HasDataPageHandlers, HasSelectionHandlers<RowClass>,
		HasColumnSortHandlers<RowClass>, IPagedDataViewer<RowClass> {

	private SingleSelDataTable<RowClass> dataTable;
	private DataPager dataPager;
	private int pageSize;
	private IPagedDataProvider<RowClass> dataProvider;
	private HandlerRegistration dataPagerHandler = null;
	private HandlerRegistration sortingHandler = null;

	public PagedDataTable(int pageSize, DataTableColumn<RowClass>... columns) {
		this.pageSize = pageSize;
		dataTable = new SingleSelDataTable<RowClass>(columns);
		dataTable.setFixedRowCount(pageSize);
		dataPager = new DataPager();

		VerticalPanel panel = new VerticalPanel();

		panel.add(dataTable);
		panel.add(dataPager);

		for (int i = 0; i < columns.length; i++) {
			if (columns[i].isSortable()) {
				setColumnSorting(i, true, false);
				break;
			}
		}

		initWidget(panel);
	}

	public RowClass getCurrentSelection() {
		return dataTable.getCurrentSelection();
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

	public IPagedDataProvider<RowClass> getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(IPagedDataProvider<RowClass> provider) {
		if (dataPagerHandler != null) {
			dataPagerHandler.removeHandler();
			dataPagerHandler = null;
		}
		if (sortingHandler != null ) {
			sortingHandler.removeHandler();
			sortingHandler = null;
		}

		this.dataProvider = provider;

		if (this.dataProvider != null) {
			dataPagerHandler = dataPager
					.addDataPageHandler(new DataPageHandler() {
						public void onDataPage(DataPageEvent event) {
							if (dataProvider != null)
								dataProvider.updateDataPage(event
										.getPageNumber(), PagedDataTable.this);
						}
					});
			sortingHandler = dataTable.addColumnSortHandler(new ColumnSortHandler<RowClass>() {
				public void onColumnSort(ColumnSortEvent<RowClass> event) {
					if (dataProvider != null)
						dataProvider.updateDataPage(dataPager.getCurrentPage(), PagedDataTable.this);
				}
			});
			this.dataProvider.updateDataPage(dataPager.getCurrentPage(), this);
		}
	}

	public int getPageSize() {
		return pageSize;
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
