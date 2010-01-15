package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.MenuBar;

public class ContextMenu extends DecoratedPopupPanel {
	private MenuBar menuBar;

	public ContextMenu() {
		super(true, false);

		menuBar = new MenuBar(true);
		setWidget(menuBar);
		setStyleName("utils-MenuBarPopup");
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

}
