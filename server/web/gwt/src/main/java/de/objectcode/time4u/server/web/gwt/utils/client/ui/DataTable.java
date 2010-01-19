package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasContextMenuHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import de.objectcode.time4u.server.web.gwt.utils.client.event.ColumnSortEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.event.ColumnSortHandler;
import de.objectcode.time4u.server.web.gwt.utils.client.event.HasColumnSortHandlers;

public class DataTable<RowClass> extends ExtendedFlexTable implements
		HasSelectionHandlers<RowClass>, HasColumnSortHandlers<RowClass>,
		HasDoubleClickHandlers, HasContextMenuHandlers {

	DataTableColumn<RowClass>[] columns;
	List<RowClass> rows = new ArrayList<RowClass>();
	RowClass currentSelection;
	int currentSelectionRowIndex;
	ContextMenu contextMenu;

	public DataTable(DataTableColumn<RowClass>... columns) {
		this(true, columns);
	}

	public DataTable(boolean showHeader, DataTableColumn<RowClass>... columns) {
		this.columns = columns;

		if (showHeader) {
			super.setHeaders(columns);

			setHeaderStyleName("utils-dataTable-header");
			for (int i = 0; i < columns.length; i++)
				if (columns[i].isSortable())
					setHeaderStyleName(i, "utils-dataTable-header-sortable");
		}

		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cell cell = getCellForEvent(event);
				if (cell != null) {
					int rowNum = cell.getRowIndex();

					if (rowNum == -1)
						onSort(cell.getCellIndex());
					else if (rowNum >= 0 && rowNum < rows.size()) {
						RowClass row = rows.get(rowNum);

						onSelection(row, rowNum, true);
					}
				}
			}
		});
	}

	public void addRow(RowClass row) {
		int rowNum = rows.size();

		rows.add(row);

		for (int i = 0; i < columns.length; i++) {
			DataTableColumn<RowClass> column = columns[i];

			if (column.isWidget()) {
				WidgetDataTableColumn<RowClass> widgetColumn = (WidgetDataTableColumn<RowClass>) column;

				setWidget(rowNum, i, widgetColumn.createCellWidget());
				widgetColumn.updateCellWidget(getWidget(rowNum, i), row);
			} else {
				TextDataTableColumn<RowClass> textColumn = (TextDataTableColumn<RowClass>) column;

				setText(rowNum, i, textColumn.getCellText(row));
			}
		}
		getRowFormatter().setStyleName(rowNum, "utils-dataTable-row");
		getRowFormatter().addStyleName(
				rowNum,
				rowNum % 2 == 0 ? "utils-dataTable-row-even"
						: "utils-dataTable-row-odd");
	}

	public void setFixedRowCount(int rowCount) {
		rows.clear();

		for (int rowNum = 0; rowNum < rowCount; rowNum++) {
			rows.add(null);

			for (int i = 0; i < columns.length; i++) {
				DataTableColumn<RowClass> column = columns[i];

				if (column.isWidget()) {
					WidgetDataTableColumn<RowClass> widgetColumn = (WidgetDataTableColumn<RowClass>) column;

					setWidget(rowNum, i, widgetColumn.createCellWidget());
				} else {
					setText(rowNum, i, "");
				}
			}
			getRowFormatter().setStyleName(rowNum, "utils-dataTable-row-empty");
		}
	}

	public void setRow(int rowNum, RowClass row) {
		rows.set(rowNum, row);

		for (int i = 0; i < columns.length; i++) {
			DataTableColumn<RowClass> column = columns[i];

			if (column.isWidget()) {
				WidgetDataTableColumn<RowClass> widgetColumn = (WidgetDataTableColumn<RowClass>) column;

				widgetColumn.updateCellWidget(getWidget(rowNum, i), row);
			} else {
				if (row != null) {
					TextDataTableColumn<RowClass> textColumn = (TextDataTableColumn<RowClass>) column;

					setText(rowNum, i, textColumn.getCellText(row));
				} else {
					setText(rowNum, i, "");
				}
			}
		}
		getRowFormatter().setStyleName(
				rowNum,
				row != null ? "utils-dataTable-row"
						: "utils-dataTable-row-empty");
		if (row != null) {
			getRowFormatter().addStyleName(
					rowNum,
					rowNum % 2 == 0 ? "utils-dataTable-row-even"
							: "utils-dataTable-row-odd");
		}
	}

	public void updateSelection() {
		currentSelectionRowIndex = -1;

		for (int i = 0; i < rows.size(); i++) {
			RowClass row = rows.get(i);
			if (row != null && row.equals(currentSelection)) {
				getRowFormatter().setStyleName(i,
						"utils-dataTable-row-selected");
				currentSelectionRowIndex = i;
			}
		}
	}

	@Override
	public void removeAllRows() {
		super.removeAllRows();
		rows.clear();
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<RowClass> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	public HandlerRegistration addContextMenuHandler(ContextMenuHandler handler) {
		return addDomHandler(handler, ContextMenuEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	public HandlerRegistration addColumnSortHandler(
			ColumnSortHandler<RowClass> handler) {
		return addHandler(handler, ColumnSortEvent.getType());
	}

	public void setContextMenu(ContextMenu menu) {
		if (this.contextMenu == null) {
			contextMenu = menu;

			addContextMenuHandler(new ContextMenuHandler() {
				public void onContextMenu(ContextMenuEvent event) {
					event.preventDefault();
					final int x = event.getNativeEvent().getClientX();
					final int y = event.getNativeEvent().getClientY();

					contextMenu.setPopupPosition(x, y);
					contextMenu.show();
				}
			});
		}
	}

	private void onSelection(RowClass row, int rowIndex, boolean fireEvents) {
		if (currentSelection != null && currentSelectionRowIndex >= 0) {
			getRowFormatter().setStyleName(currentSelectionRowIndex,
					"utils-dataTable-row");
			getRowFormatter()
					.addStyleName(
							currentSelectionRowIndex,
							currentSelectionRowIndex % 2 == 0 ? "utils-dataTable-row-even"
									: "utils-dataTable-row-odd");
		}
		currentSelection = row;
		currentSelectionRowIndex = rowIndex;

		if (currentSelection != null) {
			// Select the item and fire the selection event.
			getRowFormatter().setStyleName(rowIndex,
					"utils-dataTable-row-selected");
			if (fireEvents) {
				SelectionEvent.fire(this, currentSelection);
			}
		}
	}

	private void onSort(int columnIndex) {
		if (columnIndex >= 0 && columnIndex < columns.length) {
			ColumnSortEvent.<RowClass> fire(this, columns[columnIndex]);
		}
	}
}
