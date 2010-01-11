package de.objectcode.time4u.server.web.gwt.main.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

import de.objectcode.time4u.server.web.gwt.login.client.LoginService;
import de.objectcode.time4u.server.web.gwt.login.client.LoginServiceAsync;
import de.objectcode.time4u.server.web.gwt.main.client.ui.CalendarView;
import de.objectcode.time4u.server.web.gwt.main.client.ui.ProjectTree;
import de.objectcode.time4u.server.web.gwt.main.client.ui.TaskList;
import de.objectcode.time4u.server.web.gwt.main.client.ui.WorkItemList;
import de.objectcode.time4u.server.web.gwt.utils.client.Utils;

public class MainPage implements EntryPoint {
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	SelectionManager selectionManager = new SelectionManager();

	public void onModuleLoad() {
		loginService.login("junglas", "junglas", new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(Boolean result) {
				RootPanel.get("projectTree").add(new ProjectTree(selectionManager));

				RootPanel.get("taskList").add(new TaskList(selectionManager));

				RootPanel.get("calendarView").add(new CalendarView(selectionManager));
				
				RootPanel.get("workItemList").add(new WorkItemList(selectionManager));
			}
		});
		
		MenuBar mainMenu = new MenuBar();

		MenuBar reportMenu = new MenuBar(true);
		mainMenu.addItem("Report", reportMenu);

		MenuBar adminMenu = new MenuBar(true);
		mainMenu.addItem("Admin", adminMenu);

		mainMenu.addSeparator();
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
		RootPanel.get("mainMenu").add(mainMenu);

	}
}
