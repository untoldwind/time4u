package de.objectcode.time4u.server.web.gwt.webclient.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.IModuleCallback;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.SwitchableLayoutPanel;
import de.objectcode.time4u.server.web.gwt.webclient.client.ui.WebClientPanel;

public class WebClientModule {
	private static WebClientModule instance = null;

	private WebClientPanel webClientPanel = new WebClientPanel();

	public WebClientPanel getWebClientPanel() {
		return webClientPanel;
	}

	public static void createAsync(
			final IModuleCallback<WebClientModule> callback) {
		GWT.runAsync(WebClientModule.class, new RunAsyncCallback() {
			public void onFailure(Throwable err) {
				Window.alert("Client error" + err);
			}

			public void onSuccess() {
				if (instance == null) {
					instance = new WebClientModule();
				}
				callback.onSuccess(instance);
			}
		});
	}

	public static MenuBar createMenu(final SwitchableLayoutPanel mainPanel) {
		MenuBar webClientMenu = new MenuBar(true);
		webClientMenu.addItem("Web client", new Command() {

			public void execute() {
				showWebClientPanel(mainPanel);
			}
		});

		return webClientMenu;
	}

	public static void showWebClientPanel(final SwitchableLayoutPanel mainPanel) {
		mainPanel.prepareSwitch();

		createAsync(new IModuleCallback<WebClientModule>() {
			public void onSuccess(final WebClientModule instance) {
				mainPanel.switchWidget(instance.getWebClientPanel());
			}
		});
	}
}
