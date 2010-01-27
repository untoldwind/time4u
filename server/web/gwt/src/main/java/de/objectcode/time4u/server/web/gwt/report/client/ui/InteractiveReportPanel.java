package de.objectcode.time4u.server.web.gwt.report.client.ui;

import java.util.Date;
import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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

import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableColumnType;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableData;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRowType;
import de.objectcode.time4u.server.web.gwt.report.client.service.IdLabelPair;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportService;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportServiceAsync;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportTableData;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLayoutPanel;

public class InteractiveReportPanel extends Composite {
	private static InteractiveReportPanelUiBinder uiBinder = GWT
			.create(InteractiveReportPanelUiBinder.class);

	interface InteractiveReportPanelUiBinder extends
			UiBinder<Widget, InteractiveReportPanel> {
	}

	private final ReportServiceAsync reportService = GWT
			.create(ReportService.class);

	@UiField
	LoadingLayoutPanel loadingPanel;

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

	@UiField
	ProjectBreadcrumb projectBreadcrumb;

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

		reportTable.setCallback(new InteractiveReportTable.Callback() {

			public void onColumnHeaderClick(CrossTableColumnType columnType,
					IdLabelPair idValuePair) {

				if (columnType == CrossTableColumnType.PROJECT) {
					projectBreadcrumb.append(idValuePair);
				}
			}

			public void onRowHeaderClick(CrossTableRowType rowType,
					IdLabelPair idValuePair) {
				if (rowType == CrossTableRowType.PERSON)
					showPersonWorkItemReport(idValuePair.getId());
			}

		});

		updateData();
	}

	@UiHandler( { "from", "until" })
	protected void onDateValueChange(ValueChangeEvent<Date> event) {
		updateData();
	}

	@UiHandler( { "personTeamPerson", "personTeamTeam", "projectTaskProject",
			"projectTaskTask" })
	protected void onBooleanValueChange(ValueChangeEvent<Boolean> event) {
		updateData();
	}

	@SuppressWarnings("deprecation")
	@UiHandler("prevMonth")
	protected void onPrevMonth(ClickEvent event) {
		Date fromDate = from.getValue();

		from.setValue(new Date(fromDate.getYear()
				- (fromDate.getMonth() == 1 ? 1 : 0),
				fromDate.getMonth() == 1 ? 12 : fromDate.getMonth() - 1,
				fromDate.getDate()), false);

		Date untilDate = until.getValue();

		until.setValue(new Date(untilDate.getYear()
				- (untilDate.getMonth() == 1 ? 1 : 0),
				untilDate.getMonth() == 1 ? 12 : untilDate.getMonth() - 1,
				untilDate.getDate()), false);

		updateData();
	}

	@SuppressWarnings("deprecation")
	@UiHandler("nextMonth")
	protected void onNextMonth(ClickEvent event) {
		Date fromDate = from.getValue();

		from.setValue(new Date(fromDate.getYear()
				+ (fromDate.getMonth() == 12 ? 1 : 0),
				fromDate.getMonth() == 12 ? 1 : fromDate.getMonth() + 1,
				fromDate.getDate()), false);

		Date untilDate = until.getValue();

		until.setValue(new Date(untilDate.getYear()
				+ (untilDate.getMonth() == 12 ? 1 : 0),
				untilDate.getMonth() == 12 ? 1 : untilDate.getMonth() + 1,
				untilDate.getDate()), false);

		updateData();
	}

	@UiHandler("projectBreadcrumb")
	protected void onProjectChange(
			ValueChangeEvent<LinkedList<IdLabelPair>> event) {
		updateData();
	}

	protected CrossTableColumnType getColumnType() {
		return projectTaskProject.getValue() ? CrossTableColumnType.PROJECT
				: CrossTableColumnType.TASK;
	}

	protected CrossTableRowType getRowType() {
		return personTeamPerson.getValue() ? CrossTableRowType.PERSON
				: CrossTableRowType.TEAM;
	}

	protected void updateData() {
		loadingPanel.block();

		reportService.generateCrossTable(getColumnType(), getRowType(),
				projectBreadcrumb.getLastProject().getId(), from.getValue(),
				until.getValue(), new AsyncCallback<CrossTableData>() {
					public void onSuccess(CrossTableData result) {
						try {
							reportTable.setData(result);
						} finally {
							loadingPanel.unblock();
						}
					}

					public void onFailure(Throwable caught) {
						loadingPanel.unblock();
						Window.alert("Server error: " + caught);
					}
				});
	}

	protected void showPersonWorkItemReport(String personId) {
		System.out.println(">>>> Start");
		final long start = System.currentTimeMillis();
		final InteractiveReportDialog dialog = new InteractiveReportDialog();

		dialog.setPopupPosition((int)(0.1 * Window.getClientWidth()), (int)(0.05 * Window.getClientHeight()));

		dialog.show();

		System.out.println(">>> 1 " + (System.currentTimeMillis() - start));
		reportService.generatePersonWorkItemReport(personId, projectBreadcrumb
				.getProjectPath(), from.getValue(), until.getValue(),
				new AsyncCallback<ReportTableData>() {

					public void onSuccess(ReportTableData result) {
						System.out.println(">>> 2 " + (System.currentTimeMillis() - start));
						dialog.setData(result);
						System.out.println(">>> 3 " + (System.currentTimeMillis() - start));
					}

					public void onFailure(Throwable caught) {
						dialog.hide();
						Window.alert("Server error: " + caught);
					}
				});
	}

}
