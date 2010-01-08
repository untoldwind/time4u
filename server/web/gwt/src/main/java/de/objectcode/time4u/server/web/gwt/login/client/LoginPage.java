package de.objectcode.time4u.server.web.gwt.login.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class LoginPage implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get("loginPanel").add(new LoginPanel());
	}

}
