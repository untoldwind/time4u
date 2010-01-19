package de.objectcode.time4u.server.web.gwt.utils.client.ui;

public class TextDataTableColumn<RowClass> extends DataTableColumn<RowClass> {

	public TextDataTableColumn(String header, String width) {
		super(header, width);
	}

	public TextDataTableColumn(String header, String width, boolean sortable) {
		super(header, width, sortable);
	}

	@Override
	public boolean isWidget() {
		return false;
	}

	public String getCellText(RowClass row) {
		return row.toString();
	}
}
