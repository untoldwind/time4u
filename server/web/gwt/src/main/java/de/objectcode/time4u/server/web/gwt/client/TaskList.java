package de.objectcode.time4u.server.web.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TaskList extends Composite implements ISelectionChangeListener {

	private static TaskListUiBinder uiBinder = GWT
			.create(TaskListUiBinder.class);

	interface TaskListUiBinder extends UiBinder<Widget, TaskList> {
	}

	private final TaskServiceAsync taskService = GWT.create(TaskService.class);

	@UiField
	FlexTable taskList;

	SelectionManager selectionManager;

	public TaskList(SelectionManager selectionManager) {
		initWidget(uiBinder.createAndBindUi(this));

		this.selectionManager = selectionManager;
		this.selectionManager.addSelectionChangeListener(this);
	}

	public void selectionChanged(SelectionChangedEvent event) {

		if ( event.getType() == SelectionChangedEvent.Type.PROJECT) {
			Project project = selectionManager.getSelectedProject();
			
			taskList.removeAllRows();
			if (project != null ) {
				taskList.setWidget(0, 0, new LoadingLabel());
				
				taskService.getTasks(project.getId(), new AsyncCallback<List<Task>>() {
					
					public void onSuccess(List<Task> result) {
						taskList.removeAllRows();
						int count = 0;
						
						for ( Task task : result) {
							Label taskLabel = new Label();
							taskLabel.setText(task.getName());
							taskList.setWidget(count++, 0, taskLabel);
						}
						
						// TODO Auto-generated method stub
						
					}
					
					public void onFailure(Throwable caught) {
					}
				});
			}
		}
	}
}
