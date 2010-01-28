package de.objectcode.time4u.server.web.gwt.main.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.admin.client.AdminModule;
import de.objectcode.time4u.server.web.gwt.login.client.LoginService;
import de.objectcode.time4u.server.web.gwt.login.client.LoginServiceAsync;
import de.objectcode.time4u.server.web.gwt.login.client.UserAccountInfo;
import de.objectcode.time4u.server.web.gwt.report.client.ReportModule;
import de.objectcode.time4u.server.web.gwt.utils.client.Utils;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.SwitchableLayoutPanel;
import de.objectcode.time4u.server.web.gwt.webclient.client.WebClientModule;

public class MainPage implements EntryPoint {
	private static MainPageUiBinder uiBinder = GWT
			.create(MainPageUiBinder.class);

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	@UiField
	MenuBar mainMenu;
	
	@UiField
	SwitchableLayoutPanel mainPanel;

	public void onModuleLoad() {
		loginService.getAuthenticatedUser(new AsyncCallback<UserAccountInfo>() {
			public void onFailure(Throwable caught) {
				Window.alert("Server error: " + caught);
			}

			public void onSuccess(UserAccountInfo result) {
				initialize(result);
			}
		});
	}
	
	void initialize(UserAccountInfo userAccountInfo) {
		final Widget mainPage = uiBinder.createAndBindUi(MainPage.this);

		mainMenu.addItem("Time4U", WebClientModule.createMenu(mainPanel));
		mainMenu.addItem("Report", ReportModule.createMenu(mainPanel));
		mainMenu.addItem("Admin", AdminModule.createMenu(mainPanel));
		
		mainMenu.addSeparator(new UserIdMenuItemSeparator(userAccountInfo.getUserId()));

		mainMenu.addItem("Logout", new Command() {
			public void execute() {
				loginService.logout(new AsyncCallback<Void>() {

					public void onSuccess(Void result) {
						Utils.redirect("LoginUI.html");
					}

					public void onFailure(Throwable caught) {
					}
				});
			}
		});
		
//		AdminModule.showAccountAdminPanel(mainPanel);
//		AdminModule.showPersonAdminPanel(mainPanel);
//		WebClientModule.showWebClientPanel(mainPanel);
		ReportModule.showInteractiveReportPanel(mainPanel);
		
		RootLayoutPanel.get().add(mainPage);
	}
}
