package de.objectcode.time4u.server.web.gwt.report.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.SingleSelDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;

public class InteractiveReportPanel extends Composite {
	private static InteractiveReportPanelUiBinder uiBinder = GWT
			.create(InteractiveReportPanelUiBinder.class);

	interface InteractiveReportPanelUiBinder extends
			UiBinder<Widget, InteractiveReportPanel> {
	}

	@UiField
	InteractiveReportTable reportTable;
	
	public InteractiveReportPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public static class InteractiveReportTable extends SingleSelDataTable<ReportRow> {

		@SuppressWarnings("unchecked")
		public InteractiveReportTable() {
			super(true, new TextDataTableColumn<ReportRow>("Person", null) {
			}, new TextDataTableColumn<ReportRow>("Project1", null) {
				
			}, new TextDataTableColumn<ReportRow>("Project1", null) {
			}
			
				);
		}
	}
}
