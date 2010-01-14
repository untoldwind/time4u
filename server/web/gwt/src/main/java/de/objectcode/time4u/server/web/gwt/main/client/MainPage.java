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

import de.objectcode.time4u.server.web.gwt.login.client.LoginService;
import de.objectcode.time4u.server.web.gwt.login.client.LoginServiceAsync;
import de.objectcode.time4u.server.web.gwt.login.client.UserAccountInfo;
import de.objectcode.time4u.server.web.gwt.report.client.ReportModule;
import de.objectcode.time4u.server.web.gwt.utils.client.Utils;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IModuleCallback;
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

		MenuBar webClientMenu = new MenuBar(true);
		mainMenu.addItem("Time4U", webClientMenu);
		webClientMenu.addItem("Web client", new Command() {
			public void execute() {
				showWebClient();
			}			
		});
		
		MenuBar reportMenu = new MenuBar(true);
		mainMenu.addItem("Report", reportMenu);
		reportMenu.addItem("Interactive Report", new Command() {					
			public void execute() {
				showInteractiveReport();
			}
		});
		

		MenuBar adminMenu = new MenuBar(true);
		mainMenu.addItem("Admin", adminMenu);

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
		
		showWebClient();
		
		RootLayoutPanel.get().add(mainPage);
	}

	void showWebClient() {
		WebClientModule.createAsync(new IModuleCallback<WebClientModule>() {
			public void onSuccess(WebClientModule instance) {
				mainPanel.setChild(instance.getWebClientPanel());
			}

			public void onUnavailable() {
			}
		});
	}
	
	void showInteractiveReport() {
		ReportModule.createAsync(new IModuleCallback<ReportModule>() {			
			public void onUnavailable() {
			}
			
			public void onSuccess(ReportModule instance) {
				mainPanel.setChild(instance.getInteractiveReportPanel());
			}
		});
	}
}
