package de.objectcode.time4u.server.web.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ProjectTree extends Composite {

	private static ProjectTreeUiBinder uiBinder = GWT
			.create(ProjectTreeUiBinder.class);

	interface ProjectTreeUiBinder extends UiBinder<Widget, ProjectTree> {
	}

	@UiField
	Tree projectTree;

	private final ProjectServiceAsync projectService = GWT
			.create(ProjectService.class);

	public ProjectTree(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

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
}