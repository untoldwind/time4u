package de.objectcode.time4u.server.web.gwt.utils.client.ui;

public abstract class DataTableColumn<RowClass> extends TableHeader {
	
	public DataTableColumn(String header, String width) {
		super(header, width);
	}

	public abstract boolean isWidget();
}
