package de.objectcode.time4u.server.web.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ProjectTree extends Composite {

	private static ProjectTreeUiBinder uiBinder = GWT
			.create(ProjectTreeUiBinder.class);

	interface ProjectTreeUiBinder extends UiBinder<Widget, ProjectTree> {
	}

	@UiField
	Button button;

	@UiField
	Tree projectTree;

	private final ProjectServiceAsync projectService = GWT
			.create(ProjectService.class);

	public ProjectTree(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);

		projectService.getRootProjects(new AsyncCallback<List<Project>>() {

			public void onSuccess(List<Project> result) {
				for (Project project : result) {
					TreeItem item = projectTree.addItem(project.getName());

					item.addItem("");
				}
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

}
