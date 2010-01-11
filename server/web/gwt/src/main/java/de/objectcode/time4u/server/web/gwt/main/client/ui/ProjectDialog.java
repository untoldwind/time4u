package de.objectcode.time4u.server.web.gwt.main.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.main.client.service.Project;
import de.objectcode.time4u.server.web.gwt.main.client.service.ProjectService;
import de.objectcode.time4u.server.web.gwt.main.client.service.ProjectServiceAsync;

public class ProjectDialog extends DialogBox {
	private static ProjectDialogUiBinder uiBinder = GWT
			.create(ProjectDialogUiBinder.class);

	interface ProjectDialogUiBinder extends UiBinder<Widget, ProjectDialog> {
	}

	private final ProjectServiceAsync projectService = GWT
			.create(ProjectService.class);

	Project parentProject;
	Project project;

	@UiField
	TextBox parent;

	@UiField
	TextBox name;

	@UiField
	CheckBox active;

	boolean ok = false;

	public ProjectDialog(Project parentProject) {
		super(true, true);

		this.parentProject = parentProject;
		this.project = new Project();
		this.project.setParentId(parentProject != null ? parentProject.getId() : null);

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
		this.project = project;

		setText("Edit Project");
		setWidget(uiBinder.createAndBindUi(this));

		if (parentProject == null)
			parent.setText("[Root]");
		else
			parent.setText(parentProject.getName());

		name.setValue(project.getName());
		active.setValue(project.isActive());
	}

	@UiHandler("okButton")
	void onOk(ClickEvent event) {
		project.setName(name.getValue());
		project.setActive(active.getValue());
		
		projectService.storeProject(project, new AsyncCallback<Void>() {			
			public void onSuccess(Void result) {
				ok = true;
				hide();
			}
			
			public void onFailure(Throwable caught) {
				Window.alert("Server Error: " + caught.toString());
			}
		});
		
	}

	@UiHandler("cancelButton")
	void onCancel(ClickEvent event) {
		ok = false;
		hide();
	}

	public boolean isOk() {
		return ok;
	}
}
