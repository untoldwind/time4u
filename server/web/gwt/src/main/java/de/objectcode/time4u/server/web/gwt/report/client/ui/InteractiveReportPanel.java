package de.objectcode.time4u.server.web.gwt.report.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.DataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TableHeader;

public class InteractiveReportPanel extends Composite {
	private static InteractiveReportPanelUiBinder uiBinder = GWT
			.create(InteractiveReportPanelUiBinder.class);

	interface InteractiveReportPanelUiBinder extends
			UiBinder<Widget, InteractiveReportPanel> {
	}

	@UiField
	DataTable reportTable;
	
	public InteractiveReportPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		reportTable.setHeaders(new TableHeader("Person"), new TableHeader("Project1"), new TableHeader("Project2"));
	}
}
