package de.objectcode.time4u.server.web.gwt.main.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.main.client.service.Project;

public class ProjectDialog extends DialogBox {
	private static ProjectDialogUiBinder uiBinder = GWT
			.create(ProjectDialogUiBinder.class);

	interface ProjectDialogUiBinder extends UiBinder<Widget, ProjectDialog> {
	}

	Project parentProject;

	@UiField
	TextBox parent;
	
	@UiField
	TextBox name;

	@UiField
	CheckBox active;

	
	public ProjectDialog(Project parentProject) {
		super(true, true);

		this.parentProject = parentProject;

		setText("New Project");
		setWidget(uiBinder.createAndBindUi(this));

		if (parentProject == null)
			parent.setText("[Root]");
		else
			parent.setText(parentProject.getName());
	}

	public ProjectDialog(Project parentProject, Project project) {
		super(true, true);

		this.parentProject = parentProject;

		setText("Edit Project");
		setWidget(uiBinder.createAndBindUi(this));

		if (parentProject == null)
			parent.setText("[Root]");
		else
			parent.setText(parentProject.getName());
		
		name.setText(project.getName());
		active.setValue(project.isActive());
	}

	@UiHandler("cancelButton")
	void onCancel(ClickEvent event) {
		hide();
	}
}
