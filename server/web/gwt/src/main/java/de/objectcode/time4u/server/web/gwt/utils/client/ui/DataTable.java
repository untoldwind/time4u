package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DataTable extends ExtendedFlexTable implements
		HasSelectionHandlers<DataTableRow> {

	List<DataTableRow> rows = new ArrayList<DataTableRow>();
	DataTableRow currentSelection;

	public DataTable() {
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cell cell = getCellForEvent(event);
				if (cell != null) {
					int rowNum = cell.getRowIndex();

					if (rowNum >= 0 && rowNum < rows.size()) {
						DataTableRow row = rows.get(rowNum);

						onSelection(row, true);
					}
				}
			}
		});
	}

	@Override
	public void setHeaders(Object... headers) {
		super.setHeaders(headers);
		
		setHeaderStyleName("utils-dataTable-header");
	}

	public void addRow(DataTableRow row) {
		int rowNum = rows.size();
		Object[] columns = row.getColumns();

		row.setRowIndex(rowNum);
		rows.add(row);

		for (int i = 0; i < columns.length; i++) {
			Object column = columns[i];

			if (column instanceof Widget) {
				setWidget(rowNum, i, (Widget) column);
			} else if (column != null) {
				setWidget(rowNum, i, new Label(column.toString()));
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
			SelectionHandler<DataTableRow> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	private void onSelection(DataTableRow row, boolean fireEvents) {
		if (currentSelection != null) {
			currentSelection.setSelected(false);
			getRowFormatter().setStyleName(currentSelection.getRowIndex(), "utils-dataTable-row");
		}
		currentSelection = row;

		if (currentSelection != null) {
			// Select the item and fire the selection event.
			currentSelection.setSelected(true);
			getRowFormatter().setStyleName(currentSelection.getRowIndex(), "utils-dataTable-row-selected");
			if (fireEvents) {
				SelectionEvent.fire(this, currentSelection);
			}
		}
	}

}
