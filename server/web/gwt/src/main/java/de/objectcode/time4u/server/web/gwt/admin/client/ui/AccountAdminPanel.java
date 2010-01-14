package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AccountAdminPanel extends Composite {

	private static AccountAdminPanelUiBinder uiBinder = GWT
			.create(AccountAdminPanelUiBinder.class);

	interface AccountAdminPanelUiBinder extends
			UiBinder<Widget, AccountAdminPanel> {
	}

	public AccountAdminPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
