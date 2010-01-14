package de.objectcode.time4u.server.web.gwt.report.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;

import de.objectcode.time4u.server.web.gwt.report.client.ui.InteractiveReportPanel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.IModuleCallback;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.SwitchableLayoutPanel;

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
				Window.alert("Client error" + err);
			}

			public void onSuccess() {
				if (instance == null) {
					instance = new ReportModule();
				}
				callback.onSuccess(instance);
			}
		});
	}

	public static MenuBar createMenu(final SwitchableLayoutPanel mainPanel) {
		MenuBar reportMenu = new MenuBar(true);
		reportMenu.addItem("Interactive Report", new Command() {

			public void execute() {
				createAsync(new IModuleCallback<ReportModule>() {
					public void onSuccess(ReportModule instance) {
						mainPanel
								.setChild(instance.getInteractiveReportPanel());
					}
				});
			}
		});

		return reportMenu;
	}
}
