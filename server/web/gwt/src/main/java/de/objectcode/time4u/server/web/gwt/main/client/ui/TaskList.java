package de.objectcode.time4u.server.web.gwt.main.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.main.client.ISelectionChangeListener;
import de.objectcode.time4u.server.web.gwt.main.client.SelectionChangedEvent;
import de.objectcode.time4u.server.web.gwt.main.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.main.client.service.Project;
import de.objectcode.time4u.server.web.gwt.main.client.service.Task;
import de.objectcode.time4u.server.web.gwt.main.client.service.TaskService;
import de.objectcode.time4u.server.web.gwt.main.client.service.TaskServiceAsync;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.DataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.DataTableRow;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLabel;

public class TaskList extends Composite implements ISelectionChangeListener {

	private static TaskListUiBinder uiBinder = GWT
			.create(TaskListUiBinder.class);

	interface TaskListUiBinder extends UiBinder<Widget, TaskList> {
	}

	private final TaskServiceAsync taskService = GWT.create(TaskService.class);

	@UiField
	DataTable taskList;

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
			if (project != null) {
				taskList.setWidget(0, 0, new LoadingLabel());

				taskService.getTasks(project.getId(),
						new AsyncCallback<List<Task>>() {

							public void onSuccess(List<Task> result) {
								taskList.removeAllRows();

								for (Task task : result) {
									taskList.addRow(new DataTableRow(task, task
											.getName()));
								}
							}

							public void onFailure(Throwable caught) {
							}
						});
			}
		}
	}

	@UiHandler("taskList")
	void onTableSelection(SelectionEvent<DataTableRow> event) {
		DataTableRow row = event.getSelectedItem();

		if (row != null)
			selectionManager.selectTask((Task) row.getUserObject());
		else
			selectionManager.selectTask(null);
	}
}
