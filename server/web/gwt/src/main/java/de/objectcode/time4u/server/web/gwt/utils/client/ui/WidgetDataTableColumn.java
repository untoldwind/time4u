package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WidgetDataTableColumn<RowClass> extends DataTableColumn<RowClass> {

	public WidgetDataTableColumn(String header, String width) {
		super(header, width);
	}

	@Override
	public boolean isWidget() {
		return false;
	}

	public Widget createCellWidget(RowClass row) {
		return new Label(row.toString());
	}

	public void updateCellWidget(Widget widget, RowClass row) {
		Label label = (Label) widget;

		label.setText(row.toString());
	}

}
