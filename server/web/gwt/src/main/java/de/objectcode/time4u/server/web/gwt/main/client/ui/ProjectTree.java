package de.objectcode.time4u.server.web.gwt.main.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.main.client.MainClientBundle;
import de.objectcode.time4u.server.web.gwt.main.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.main.client.service.Project;
import de.objectcode.time4u.server.web.gwt.main.client.service.ProjectService;
import de.objectcode.time4u.server.web.gwt.main.client.service.ProjectServiceAsync;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLabel;

public class ProjectTree extends Composite {

	private static ProjectTreeUiBinder uiBinder = GWT
			.create(ProjectTreeUiBinder.class);

	interface ProjectTreeUiBinder extends UiBinder<Widget, ProjectTree> {
	}

	@UiField(provided = true)
	MainClientBundle resources = MainClientBundle.INSTANCE;

	@UiField
	Tree projectTree;

	@UiField
	PushButton newProject;

	@UiField
	PushButton editProject;

	@UiField
	PushButton deleteProject;

	private final ProjectServiceAsync projectService = GWT
			.create(ProjectService.class);

	public ProjectTree() {
		initWidget(uiBinder.createAndBindUi(this));

		projectTree.addItem(new LoadingLabel());
		projectTree.setAnimationEnabled(true);

		refresh();
	}

	@UiHandler("projectTree")
	public void onOpen(OpenEvent<TreeItem> event) {
		final TreeItem item = event.getTarget();
		Project project = (Project) item.getUserObject();

		if (project != null) {

			projectService.getChildProjects(project.getId(),
					new AsyncCallback<List<Project>>() {
						public void onSuccess(List<Project> result) {
							item.removeItems();

							for (Project project : result) {
								TreeItem subItem = item
										.addItem("<span class=\""
												+ (project.isActive() ? "projectTree-active"
														: "projectTree-inactive")
												+ "\">" + project.getName()
												+ "</span>");

								subItem.setUserObject(project);

								if (project.isHasChildren())
									subItem.addItem("");
							}
						}

						public void onFailure(Throwable caught) {
							Window.alert("Server Error: " + caught.toString());
						}
					});
		}
	}

	@UiHandler("projectTree")
	public void onSelection(SelectionEvent<TreeItem> event) {
		final TreeItem item = event.getSelectedItem();
		Project project = (Project) item.getUserObject();

		SelectionManager.INSTANCE.selectProject(project);
		editProject.setEnabled(project != null);
		deleteProject.setEnabled(project != null);
	}

	@UiHandler("newProject")
	public void onNewProject(ClickEvent event) {
		TreeItem item = projectTree.getSelectedItem();

		ProjectDialog dialog = new ProjectDialog(item != null ? (Project) item
				.getUserObject() : null);

		dialog.center();
		dialog.show();
	}

	@UiHandler("editProject")
	public void onEditProject(ClickEvent event) {
		TreeItem item = projectTree.getSelectedItem();

		if (item != null) {
			TreeItem parent = item.getParentItem();

			ProjectDialog dialog = new ProjectDialog(
					parent != null ? (Project) parent.getUserObject() : null,
					(Project) item.getUserObject());

			dialog.center();
			dialog.show();
		}
	}

	void refresh() {
		projectService.getRootProjects(new AsyncCallback<List<Project>>() {

			public void onSuccess(List<Project> result) {
				projectTree.removeItems();

				for (Project project : result) {
					TreeItem item = projectTree.addItem("<span class=\""
							+ (project.isActive() ? "projectTree-active"
									: "projectTree-inactive") + "\">"
							+ project.getName() + "</span>");

					item.setUserObject(project);

					if (project.isHasChildren()) {
						item.addItem(new LoadingLabel());
					}
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert("Server Error: " + caught.toString());
			}
		});
	}
}