package de.objectcode.time4u.server.web.gwt.utils.client.ui;

public abstract class DataTableColumn<RowClass> extends TableHeader {

	private boolean sortable;
	private ColumnSorting sorting;

	public DataTableColumn(String header, String width) {
		this(header, width, false);
	}

	public DataTableColumn(String header, String width, boolean sortable) {
		super(header, width);

		this.sortable = sortable;
		this.sorting = ColumnSorting.NONE;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public ColumnSorting getSorting() {
		return sorting;
	}

	public void setSorting(ColumnSorting sorting) {
		this.sorting = sorting;
	}

	public abstract boolean isWidget();
}
