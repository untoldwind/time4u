package de.objectcode.time4u.server.web.gwt.report.client.ui;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTable;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableColumnType;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRowType;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportService;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportServiceAsync;

public class InteractiveReportPanel extends Composite {
	private static InteractiveReportPanelUiBinder uiBinder = GWT
			.create(InteractiveReportPanelUiBinder.class);

	interface InteractiveReportPanelUiBinder extends
			UiBinder<Widget, InteractiveReportPanel> {
	}

	private final ReportServiceAsync reportService = GWT
			.create(ReportService.class);

	@UiField
	InteractiveReportTable reportTable;

	@UiField
	DateBox from;

	@UiField
	DateBox until;

	@UiField
	RadioButton personTeamPerson;

	@UiField
	RadioButton projectTaskProject;

	@SuppressWarnings("deprecation")
	public InteractiveReportPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		Date now = new Date();
		Date fromDate = new Date(now.getYear(), now.getMonth(), 1);

		from.setValue(fromDate, false);

		Date untilDate = new Date(fromDate.getYear()
				+ (now.getMonth() == 12 ? 1 : 0), now.getMonth() == 12 ? 1
				: now.getMonth() + 1, 1);

		until.setValue(untilDate, false);

		from.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getMediumDateFormat()));
		until.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getMediumDateFormat()));

		personTeamPerson.setValue(true, false);
		projectTaskProject.setValue(true, false);

		updateData();
	}

	@UiHandler( { "from", "until" })
	protected void onDateValueChange(ValueChangeEvent<Date> event) {
		updateData();
	}

	@UiHandler( { "personTeamPerson","personTeamTeam", "projectTaskProject","projectTaskTask" })
	protected void onBooleanValueChange(ValueChangeEvent<Boolean> event) {
		updateData();
	}

	protected void updateData() {
		CrossTableColumnType columnType = projectTaskProject.getValue() ? CrossTableColumnType.PROJECT : CrossTableColumnType.TASK;
		CrossTableRowType rowType = personTeamPerson.getValue() ? CrossTableRowType.PERSON : CrossTableRowType.TEAM;

		reportService.generateCrossTable(columnType, rowType, null , from.getValue(), until.getValue(), new AsyncCallback<CrossTable>() {
			public void onSuccess(CrossTable result) {
				reportTable.setData(result);
			}

			public void onFailure(Throwable caught) {
				Window.alert("Server error: " + caught);
			}
		});
	}
}
