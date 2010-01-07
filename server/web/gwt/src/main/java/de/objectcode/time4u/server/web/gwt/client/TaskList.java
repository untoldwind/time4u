package de.objectcode.time4u.server.web.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class TaskList extends Composite implements ISelectionChangeListener {

	private static TaskListUiBinder uiBinder = GWT
			.create(TaskListUiBinder.class);

	interface TaskListUiBinder extends UiBinder<Widget, TaskList> {
	}

	private final TaskServiceAsync taskService = GWT.create(TaskService.class);

	@UiField
	FlexTable taskList;

	List<Task> taskListModel;

	SelectionManager selectionManager;

	public TaskList(SelectionManager selectionManager) {
		initWidget(uiBinder.createAndBindUi(this));

		this.selectionManager = selectionManager;
		this.selectionManager.addSelectionChangeListener(this);
	}

	public void selectionChanged(SelectionChangedEvent event) {

		if (event.getType() == SelectionChangedEvent.Type.PROJECT) {
			Project project = selectionManager.getSelectedProject();

			taskList.removeAllRows();
			taskListModel = null;
			if (project != null) {
				taskList.setWidget(0, 0, new LoadingLabel());

				taskService.getTasks(project.getId(),
						new AsyncCallback<List<Task>>() {

							public void onSuccess(List<Task> result) {
								taskList.removeAllRows();
								int count = 0;

								taskListModel = result;
								for (Task task : taskListModel) {
									Label taskLabel = new Label();
									taskLabel.setText(task.getName());
									taskList.setWidget(count, 0, taskLabel);
									taskList.getRowFormatter().setStyleName(
											count, "task-Label");
									count++;
								}

								// TODO Auto-generated method stub

							}

							public void onFailure(Throwable caught) {
							}
						});
			}
		}
	}

	@UiHandler("taskList")
	void onTableClicked(ClickEvent event) {
		if (taskListModel == null)
			return;

		// Select the row that was clicked (-1 to account for header row).
		Cell cell = taskList.getCellForEvent(event);
		if (cell != null) {
			int row = cell.getRowIndex();

			for (int i = 0; i < taskList.getRowCount(); i++) {
				taskList.getRowFormatter().setStyleName(i,
						i == row ? "task-Label-selected" : "task-Label");
			}
			selectionManager.selectTask(taskListModel.get(row));
		}
	}

}
