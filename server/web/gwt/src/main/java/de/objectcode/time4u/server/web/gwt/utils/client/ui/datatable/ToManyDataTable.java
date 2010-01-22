package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;


public class ToManyDataTable<RowClass> extends Composite implements IDataView<RowClass> {
	private static UI uiBinder = GWT.create(UI.class);

	@SuppressWarnings("unchecked")
	interface UI extends UiBinder<Widget, ToManyDataTable> {
	}

	DataTable<RowClass> dataTable;

	@UiField
	ScrollPanel scrollPanel;
	
	public ToManyDataTable(DataTableColumn<RowClass>... columns) {
		this(true, columns);
	}

	public ToManyDataTable(boolean showHeader,
			DataTableColumn<RowClass>... columns) {
		initWidget(uiBinder.createAndBindUi(this));
		
		dataTable = new DataTable<RowClass>(showHeader, columns);
		dataTable.setWidth("100%");
		
		scrollPanel.add(dataTable);
	}

	public void setData(Collection<RowClass> data) {
		dataTable.setData(data);
	}
}
