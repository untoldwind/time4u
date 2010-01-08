package de.objectcode.time4u.server.web.gwt.main.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLabel;

public class ProjectTree extends Composite  {

	private static ProjectTreeUiBinder uiBinder = GWT
			.create(ProjectTreeUiBinder.class);

	interface ProjectTreeUiBinder extends UiBinder<Widget, ProjectTree> {
	}

	@UiField
	Tree projectTree;

	SelectionManager selectionManager;
	
	private final ProjectServiceAsync projectService = GWT
			.create(ProjectService.class);

	public ProjectTree(SelectionManager selectionManager) {
		initWidget(uiBinder.createAndBindUi(this));

		this.selectionManager = selectionManager;
		
		projectTree.addItem(new LoadingLabel());

		projectService.getRootProjects(new AsyncCallback<List<Project>>() {

			public void onSuccess(List<Project> result) {
				projectTree.removeItems();

				for (Project project : result) {
					TreeItem item = projectTree.addItem(project.getName());

					item.setUserObject(project);

					if (project.isHasChildren()) {
						item.addItem(new LoadingLabel());
					}
				}
			}

			public void onFailure(Throwable caught) {
			}
		});
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
								TreeItem subItem = item.addItem(project
										.getName());

								subItem.setUserObject(project);

								if (project.isHasChildren())
									subItem.addItem("");
							}
						}

						public void onFailure(Throwable caught) {
						}
					});
		}
	}
	
	@UiHandler("projectTree")
	public void onSelection(SelectionEvent<TreeItem> event) {
		final TreeItem item = event.getSelectedItem();
		Project project = (Project) item.getUserObject();

		selectionManager.selectProject(project);
	}
}