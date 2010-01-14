package de.objectcode.time4u.server.web.gwt.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import de.objectcode.time4u.server.web.gwt.admin.client.ui.AccountAdminPanel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IModuleCallback;

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
				callback.onUnavailable();
			}

			public void onSuccess() {
				if (instance == null) {
					instance = new AdminModule();
				}
				callback.onSuccess(instance);
			}
		});
	}
}
