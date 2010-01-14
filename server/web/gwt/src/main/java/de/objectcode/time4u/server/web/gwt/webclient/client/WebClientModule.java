package de.objectcode.time4u.server.web.gwt.webclient.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.IModuleCallback;
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
				callback.onUnavailable();
			}

			public void onSuccess() {
				if (instance == null) {
					instance = new WebClientModule();
				}
				callback.onSuccess(instance);
			}
		});
	}

}
