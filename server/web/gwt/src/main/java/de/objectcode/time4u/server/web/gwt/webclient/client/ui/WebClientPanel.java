package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WebClientPanel extends Composite {

	private static WebClientPanelUiBinder uiBinder = GWT
			.create(WebClientPanelUiBinder.class);

	interface WebClientPanelUiBinder extends UiBinder<Widget, WebClientPanel> {
	}

	public WebClientPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
