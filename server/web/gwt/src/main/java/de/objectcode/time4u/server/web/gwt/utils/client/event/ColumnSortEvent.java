package de.objectcode.time4u.server.web.gwt.utils.client.event;

import com.google.gwt.event.shared.GwtEvent;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.DataTableColumn;

public class ColumnSortEvent<RowClass> extends
		GwtEvent<ColumnSortHandler<RowClass>> {
	private static Type<ColumnSortHandler<?>> TYPE;

	private final int columnIndex;
	private final DataTableColumn<RowClass> sortColumn;

	protected ColumnSortEvent(int columnIndex, DataTableColumn<RowClass> sortColumn) {
		this.columnIndex = columnIndex;
		this.sortColumn = sortColumn;
	}
	
	public int getColumnIndex() {
		return columnIndex;
	}

	public DataTableColumn<RowClass> getSortColumn() {
		return sortColumn;
	}

	public static <RowClass> void fire(HasColumnSortHandlers<RowClass> source, int columnIndex, DataTableColumn<RowClass> sortColumn) {
		if (TYPE != null) {
			ColumnSortEvent<RowClass> event = new ColumnSortEvent<RowClass>(columnIndex, sortColumn);
			source.fireEvent(event);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Type<ColumnSortHandler<RowClass>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(ColumnSortHandler<RowClass> handler) {
		handler.onColumnSort(this);
	}

	public static Type<ColumnSortHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<ColumnSortHandler<?>>();
		}
		return TYPE;
	}

}
