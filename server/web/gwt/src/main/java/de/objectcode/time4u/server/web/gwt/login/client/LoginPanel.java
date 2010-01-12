package de.objectcode.time4u.server.web.gwt.login.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
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

import de.objectcode.time4u.server.web.gwt.utils.client.Utils;

public class LoginPanel extends Composite {
	private static LoginPanelUiBinder uiBinder = GWT
			.create(LoginPanelUiBinder.class);

	interface LoginPanelUiBinder extends UiBinder<Widget, LoginPanel> {
	}

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

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
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			password.setFocus(true);
		}
	}

	@UiHandler("password")
	void onPasswordChange(KeyUpEvent event) {
		login.setEnabled(userId.getValue().length() > 0
				&& password.getValue().length() > 0);
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			login();
		}
	}

	@UiHandler("login")
	void onLoginClick(ClickEvent event) {
		login();
	}

	private void login() {
		String url = GWT.getHostPageBaseURL() + "j_spring_security_check";
		StringBuffer data = new StringBuffer();
		data.append("j_username=");
		data.append(URL.encode(userId.getValue()));
		data.append("&j_password=");
		data.append(URL.encode(password.getValue()));
		System.out.println(url);

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);

		builder.setHeader("Content-Type", "application/x-www-form-urlencoded");

		try {
			builder.sendRequest(data.toString(), new RequestCallback() {

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() == 200) {
						loginSuccessful();
					} else {
						loginFailed();
					}
				}

				public void onError(Request request, Throwable exception) {
					Window.alert("Server error: " + exception);
				}

			});
		} catch (RequestException e) {
			Window.alert("Client error: " + e);
		}

		/*
		 * loginService.login(userId.getValue(), password.getValue(), new
		 * AsyncCallback<Boolean>() { public void onSuccess(Boolean result) { if
		 * (result) loginSuccessful(); else loginFailed(); }
		 * 
		 * public void onFailure(Throwable caught) { } });
		 */
	}

	private void loginFailed() {
		Window.alert("Login failed");
	}

	private void loginSuccessful() {
		Utils.redirect("MainUI.html");
	}
}
