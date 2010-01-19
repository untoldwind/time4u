package de.objectcode.time4u.server.web.gwt.utils.client.ui;

public abstract class DataTableColumn<RowClass> extends TableHeader {

	private boolean sortable;

	public DataTableColumn(String header, String width) {
		this(header, width, false);
	}

	public DataTableColumn(String header, String width, boolean sortable) {
		super(header, width);

		this.sortable = sortable;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public abstract boolean isWidget();
}
