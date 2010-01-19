package de.objectcode.time4u.server.web.gwt.utils.client.event;

import com.google.gwt.event.shared.GwtEvent;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.DataTableColumn;

public class ColumnSortEvent<RowClass> extends
		GwtEvent<ColumnSortHandler<RowClass>> {
	private static Type<ColumnSortHandler<?>> TYPE;

	private final DataTableColumn<RowClass> sortColumn;

	protected ColumnSortEvent(DataTableColumn<RowClass> sortColumn) {
		this.sortColumn = sortColumn;
	}
	
	public DataTableColumn<RowClass> getSortColumn() {
		return sortColumn;
	}

	public static <RowClass> void fire(HasColumnSortHandlers<RowClass> source, DataTableColumn<RowClass> sortColumn) {
		if (TYPE != null) {
			ColumnSortEvent<RowClass> event = new ColumnSortEvent<RowClass>(sortColumn);
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
