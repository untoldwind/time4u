package de.objectcode.time4u.server.web.gwt.login.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginPanel extends Composite {
	private static LoginPanelUiBinder uiBinder = GWT
			.create(LoginPanelUiBinder.class);

	interface LoginPanelUiBinder extends UiBinder<Widget, LoginPanel> {
	}

	@UiField
	TextBox userId;

	@UiField
	PasswordTextBox password;

	@UiField
	Button login;

	public LoginPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("userId")
	void onUserIdChange(KeyUpEvent event) {
		login.setEnabled(userId.getValue().length() > 0
				&& password.getValue().length() > 0);
	}

	@UiHandler("password")
	void onPasswordChange(KeyUpEvent event) {
		login.setEnabled(userId.getValue().length() > 0
				&& password.getValue().length() > 0);
	}

	@UiHandler("login")
	void onLoginClick(ClickEvent event) {
		StringBuffer url = new StringBuffer("MainUI.html");
		boolean first = true;

		for (Map.Entry<String, List<String>> entry : Window.Location
				.getParameterMap().entrySet()) {
			String key = entry.getKey();
			for (String value : entry.getValue()) {
				if (first)
					url.append("?");
				else
					url.append("&");
				first = false;
				url.append(key);
				url.append("=");
				url.append(URL.encode(value));
			}

		}
		Window.open(url.toString(), "_self", "");
	}
}
