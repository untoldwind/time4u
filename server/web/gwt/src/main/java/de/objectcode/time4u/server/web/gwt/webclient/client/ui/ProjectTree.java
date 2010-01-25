package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.ContextMenu;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedSplitLayoutPanel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedTree;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLabel;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.webclient.client.WebClientBundle;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Project;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.ProjectService;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.ProjectServiceAsync;

public class ProjectTree extends Composite {

	private static ProjectTreeUiBinder uiBinder = GWT
			.create(ProjectTreeUiBinder.class);

	interface ProjectTreeUiBinder extends UiBinder<Widget, ProjectTree> {
	}

	@UiField(provided = true)
	WebClientBundle resources = WebClientBundle.INSTANCE;

	@UiField
	ExtendedTree projectTree;

	@UiField
	PushButton newProject;

	@UiField
	PushButton editProject;

	@UiField
	PushButton deleteProject;

	Map<String, Project> openedProjects = new HashMap<String, Project>();

	private final ProjectServiceAsync projectService = GWT
			.create(ProjectService.class);

	public ProjectTree() {
		initWidget(uiBinder.createAndBindUi(this));

		projectTree.addItem(new LoadingLabel());
		projectTree.setAnimationEnabled(true);

		ContextMenu contextMenu = new ContextMenu();

		contextMenu.addItem("New Project", new Command() {
			public void execute() {
				System.out.println(">>> new Project");
			}
		});
		contextMenu.addItem("Edit Project", new Command() {
			public void execute() {
				System.out.println(">>> edit Project");
			}
		});

		projectTree.setContextMenu(contextMenu);

		refresh();
	}

	@UiHandler("projectTree")
	public void onOpen(OpenEvent<TreeItem> event) {
		final TreeItem item = event.getTarget();
		Project project = (Project) item.getUserObject();

		if (project != null) {
			openedProjects.put(project.getId(), project);

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

								if (openedProjects.containsKey(project.getId()))
									subItem.setState(true, true);

								if (SelectionManager.INSTANCE
										.getSelectedProject() != null)
									if (project.getId().equals(
											SelectionManager.INSTANCE
													.getSelectedProject()
													.getId()))
										subItem.setSelected(true);
							}
						}

						public void onFailure(Throwable caught) {
							Window.alert("Server Error: " + caught.toString());
						}
					});
		}
	}

	@UiHandler("projectTree")
	public void onClose(CloseEvent<TreeItem> event) {
		final TreeItem item = event.getTarget();
		Project project = (Project) item.getUserObject();

		if (project != null) {
			openedProjects.remove(project.getId());
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
				.getUserObject() : null, new IDialogCallback() {
			public void onOk() {
				refresh();
			}
		});

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
					(Project) item.getUserObject(), new IDialogCallback() {
						public void onOk() {
							refresh();
						}
					});

			dialog.center();
			dialog.show();
		}
	}

	@UiHandler("panelMin")
	protected void onPanelMinClick(ClickEvent event) {
		ExtendedSplitLayoutPanel parent = (ExtendedSplitLayoutPanel) getParent();
		ExtendedSplitLayoutPanel parentParent = (ExtendedSplitLayoutPanel) parent.getParent();

		parent.minimizeChild(this);
		parentParent.minimizeChild(parent);
	}

	@UiHandler("panelMax")
	protected void onPanelMaxClick(ClickEvent event) {
		ExtendedSplitLayoutPanel parent = (ExtendedSplitLayoutPanel) getParent();
		ExtendedSplitLayoutPanel parentParent = (ExtendedSplitLayoutPanel) parent.getParent();

		parent.maximizeChild(this);
		parentParent.maximizeChild(parent);
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

					if (openedProjects.containsKey(project.getId()))
						item.setState(true, true);

					if (SelectionManager.INSTANCE.getSelectedProject() != null)
						if (project.getId().equals(
								SelectionManager.INSTANCE.getSelectedProject()
										.getId()))
							item.setSelected(true);
				}
			}

			public void onFailure(Throwable caught) {
				Window.alert("Server Error: " + caught.toString());
			}
		});
	}
}