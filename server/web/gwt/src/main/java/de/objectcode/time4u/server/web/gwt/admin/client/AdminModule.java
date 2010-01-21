package de.objectcode.time4u.server.web.gwt.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;

import de.objectcode.time4u.server.web.gwt.admin.client.ui.AccountAdminPanel;
import de.objectcode.time4u.server.web.gwt.admin.client.ui.PersonAdminPanel;
import de.objectcode.time4u.server.web.gwt.admin.client.ui.TeamAdminPanel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IModuleCallback;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.SwitchableLayoutPanel;

public class AdminModule {
	private static AdminModule instance = null;

	private AccountAdminPanel accountAdminPanel = new AccountAdminPanel();
	private PersonAdminPanel personAdminPanel= new PersonAdminPanel();
	private TeamAdminPanel teamAdminPanel= new TeamAdminPanel();

	public AccountAdminPanel getAccountAdminPanel() {
		return accountAdminPanel;
	}

	public PersonAdminPanel getPersonAdminPanel() {
		return personAdminPanel;
	}
	
	public TeamAdminPanel getTeamAdminPanel() {
		return teamAdminPanel;
	}

	public static void createAsync(final IModuleCallback<AdminModule> callback) {
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable err) {
				Window.alert("Client error" + err);
			}

			public void onSuccess() {
				if (instance == null) {
					instance = new AdminModule();
				}
				callback.onSuccess(instance);
			}
		});
	}
	
	public static MenuBar createMenu(final SwitchableLayoutPanel mainPanel) {
		MenuBar adminMenu = new MenuBar(true);
		adminMenu.addItem("User accounts", new Command() {
			
			public void execute() {
				showAccountAdminPanel(mainPanel);
			}
		});
		adminMenu.addItem("Persons", new Command() {
			
			public void execute() {
				showPersonAdminPanel(mainPanel);
			}
		});
		adminMenu.addItem("Teams", new Command() {
			
			public void execute() {
				showTeamAdminPanel(mainPanel);
			}
		});

		return adminMenu;
	}

	public static void showAccountAdminPanel(final SwitchableLayoutPanel mainPanel) {
		mainPanel.prepareSwitch();

		createAsync(new IModuleCallback<AdminModule>() {
			public void onSuccess(AdminModule instance) {
				mainPanel.switchWidget(instance.getAccountAdminPanel());
			}
		});
	}

	public static void showPersonAdminPanel(final SwitchableLayoutPanel mainPanel) {
		mainPanel.prepareSwitch();
		
		createAsync(new IModuleCallback<AdminModule>() {
			public void onSuccess(AdminModule instance) {
				mainPanel.switchWidget(instance.getPersonAdminPanel());
			}
		});
	}

	public static void showTeamAdminPanel(final SwitchableLayoutPanel mainPanel) {
		mainPanel.prepareSwitch();
		
		createAsync(new IModuleCallback<AdminModule>() {
			public void onSuccess(AdminModule instance) {
				mainPanel.switchWidget(instance.getTeamAdminPanel());
			}
		});
	}
}
