package de.objectcode.time4u.server.web.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class MainPage implements EntryPoint {
	public static final MainClientBundle images = (MainClientBundle) GWT
			.create(MainClientBundle.class);

	SelectionManager selectionManager = new SelectionManager();
	
	public void onModuleLoad() {
		MenuBar mainMenu = new MenuBar();

		MenuBar reportMenu = new MenuBar(true);
		mainMenu.addItem("Report", reportMenu);

		MenuBar adminMenu = new MenuBar(true);
		mainMenu.addItem("Admin", adminMenu);

		mainMenu.addSeparator();
		mainMenu.addItem("Logout", new Command() {
			public void execute() {
				Window.alert("Logging out");
			}
		});
		RootPanel.get("mainMenu").add(mainMenu);

		RootPanel.get("projectTree").add(new ProjectTree(selectionManager));
		
		RootPanel.get("taskList").add(new TaskList(selectionManager));
	}
}
