package de.objectcode.time4u.server.web.gwt.admin.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;

public class AccountDetailPanel extends Composite {

	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, AccountDetailPanel> {
	}

	@UiField
	TextBox userId;

	@UiField
	TextBox givenName;

	@UiField
	TextBox surname;

	@UiField
	TextBox email;

	public AccountDetailPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setUserAccount(UserAccount userAccount) {
		userId.setText(userAccount.getUserId());
		givenName.setText(userAccount.getPerson().getGivenName());
		surname.setText(userAccount.getPerson().getSurname());
		email.setText(userAccount.getPerson().getEmail());
	}
}
