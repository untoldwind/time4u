package de.objectcode.time4u.server.web.gwt.report.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.report.client.service.ReportTableData;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLayoutPanel;

public class InteractiveReportDialog extends DialogBox {
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, InteractiveReportDialog> {
	}

	@UiField
	LoadingLayoutPanel loadingPanel;

	@UiField
	ReportTable reportTable;

	@UiField
	PushButton closeButton;

	public InteractiveReportDialog() {
		super(true, true);

		setGlassEnabled(true);

		setWidget(uiBinder.createAndBindUi(this));

		loadingPanel.setPixelSize((int) (0.8 * Window.getClientWidth()),
				(int) (0.7 * Window.getClientHeight()));
		loadingPanel.hardBlock();
	}

	public void setData(ReportTableData report) {
		loadingPanel.unblock();
		setText(report.getName());
		reportTable.setData(report);
	}

	@UiHandler("closeButton")
	protected void onCloseClick(ClickEvent event) {
		hide();
	}
}
