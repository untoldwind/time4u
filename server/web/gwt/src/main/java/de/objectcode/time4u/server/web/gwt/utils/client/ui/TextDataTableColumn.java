package de.objectcode.time4u.server.web.gwt.utils.client.ui;

public class TextDataTableColumn<RowClass> extends DataTableColumn<RowClass> {

	public TextDataTableColumn(String header, String width) {
		super(header, width);
	}

	@Override
	public boolean isWidget() {
		return false;
	}

	public String getCellText(RowClass row) {
		return row.toString();
	}
}
