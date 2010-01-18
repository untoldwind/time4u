package de.objectcode.time4u.server.web.gwt.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;

import de.objectcode.time4u.server.web.gwt.admin.client.ui.AccountAdminPanel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IModuleCallback;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.SwitchableLayoutPanel;

public class AdminModule {
	private static AdminModule instance = null;

	private AccountAdminPanel accountAdminPanel;

	public AccountAdminPanel getAccountAdminPanel() {
		if (accountAdminPanel == null)
			accountAdminPanel = new AccountAdminPanel();

		return accountAdminPanel;
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

		return adminMenu;
	}

	public static void showAccountAdminPanel(final SwitchableLayoutPanel mainPanel) {
		createAsync(new IModuleCallback<AdminModule>() {
			public void onSuccess(AdminModule instance) {
				mainPanel.setChild(instance.getAccountAdminPanel());
			}
		});
	}
}
