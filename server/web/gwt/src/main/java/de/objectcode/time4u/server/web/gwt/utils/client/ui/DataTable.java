package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class DataTable<RowClass> extends ExtendedFlexTable implements
		HasSelectionHandlers<RowClass> {

	DataTableColumn<RowClass>[] columns;
	List<RowClass> rows = new ArrayList<RowClass>();
	RowClass currentSelection;
	int currentSelectionRowIndex;

	public DataTable(DataTableColumn<RowClass>... columns) {
		this(true, columns);
	}

	public DataTable(boolean showHeader, DataTableColumn<RowClass>... columns) {
		this.columns = columns;

		if ( showHeader) {
		super.setHeaders(columns);
		
		setHeaderStyleName("utils-dataTable-header");}
		
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cell cell = getCellForEvent(event);
				if (cell != null) {
					int rowNum = cell.getRowIndex();

					if (rowNum >= 0 && rowNum < rows.size()) {
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

				setWidget(rowNum, i, widgetColumn.createCellWidget(row));
			} else {
				TextDataTableColumn<RowClass> textColumn = (TextDataTableColumn<RowClass>) column;

				setText(rowNum, i, textColumn.getCellText(row));
			}
		}
		getRowFormatter().setStyleName(rowNum, "utils-dataTable-row");
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

	private void onSelection(RowClass row, int rowIndex, boolean fireEvents) {
		if (currentSelection != null) {
			getRowFormatter().setStyleName(currentSelectionRowIndex,
					"utils-dataTable-row");
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

}
