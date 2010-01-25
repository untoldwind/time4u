package de.objectcode.time4u.server.web.gwt.report.client.ui;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

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

	@UiField
	DateBox from;

	@UiField
	DateBox until;

	@SuppressWarnings("deprecation")
	public InteractiveReportPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		Date now = new Date();
		Date fromDate = new Date(now.getYear(), now.getMonth(), 1);

		from.setValue(fromDate);

		Date untilDate = new Date(fromDate.getYear()
				+ (now.getMonth() == 12 ? 1 : 0), now.getMonth() == 12 ? 1
				: now.getMonth() + 1, 1);

		until.setValue(untilDate);
		
		from.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getMediumDateFormat()));
		until.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getMediumDateFormat()));
	}

	public static class InteractiveReportTable extends
			SingleSelDataTable<ReportRow> {

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
