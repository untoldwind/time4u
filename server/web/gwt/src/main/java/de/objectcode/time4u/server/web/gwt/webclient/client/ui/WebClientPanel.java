package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class WebClientPanel extends Composite {

	private static WebClientPanelUiBinder uiBinder = GWT
			.create(WebClientPanelUiBinder.class);

	interface WebClientPanelUiBinder extends UiBinder<Widget, WebClientPanel> {
	}

	@UiField
	Widget projectTree;

	@UiField
	Widget taskList;
	
	@UiField
	Widget calendarView;
	
	@UiField
	Widget westPanel;
	
	public WebClientPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		((SplitLayoutPanel)projectTree.getParent()).setWidgetMinSize(projectTree, 200);
		((SplitLayoutPanel)calendarView.getParent()).setWidgetMinSize(calendarView, 200);
		((SplitLayoutPanel)westPanel.getParent()).setWidgetMinSize(westPanel, 200);
	}
}
