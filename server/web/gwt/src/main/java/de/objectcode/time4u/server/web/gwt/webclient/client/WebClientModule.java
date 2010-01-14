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

	private WebClientPanel webClientPanel;
	
	public WebClientPanel getWebClientPanel() {
		if ( webClientPanel == null )
			webClientPanel = new WebClientPanel();
		
		return webClientPanel;
	}
	
	public static void createAsync(final IModuleCallback<WebClientModule> callback) {
		GWT.runAsync(new RunAsyncCallback() {
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
				createAsync(new IModuleCallback<WebClientModule>() {
					public void onSuccess(WebClientModule instance) {
						mainPanel
								.setChild(instance.getWebClientPanel());
					}
				});
			}
		});

		return webClientMenu;
	}
	
	public static void show(final SwitchableLayoutPanel mainPanel) {
		createAsync(new IModuleCallback<WebClientModule>() {
			public void onSuccess(WebClientModule instance) {
				mainPanel
						.setChild(instance.getWebClientPanel());
			}
		});
	}
}
