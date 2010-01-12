package de.objectcode.time4u.server.web.gwt.report.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.Composite;

import de.objectcode.time4u.server.web.gwt.report.client.ui.InteractiveReportPanel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IModuleCallback;

public class ReportModule {

	private static ReportModule instance = null;

	private InteractiveReportPanel interactiveReportPanel;

	public Composite getInteractiveReportPanel() {
		if (interactiveReportPanel == null)
			interactiveReportPanel = new InteractiveReportPanel();
		return interactiveReportPanel;
	}

	public static void createAsync(final IModuleCallback<ReportModule> callback) {
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable err) {
				callback.onUnavailable();
			}

			public void onSuccess() {
				if (instance == null) {
					instance = new ReportModule();
				}
				callback.onSuccess(instance);
			}
		});
	}
}
