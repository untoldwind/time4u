package de.objectcode.time4u.server.web.gwt.main.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class ProjectDialog extends DialogBox {
	private static ProjectDialogUiBinder uiBinder = GWT
			.create(ProjectDialogUiBinder.class);

	interface ProjectDialogUiBinder extends UiBinder<Widget, ProjectDialog> {
	}

	public ProjectDialog() {
		super(true, true);

		setText("New Project");
		setWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("cancelButton")
	void onCancel(ClickEvent event) {
		hide();
	}
}
