package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.ContextMenu;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedSplitLayoutPanel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLabel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.SingleSelDataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;
import de.objectcode.time4u.server.web.gwt.webclient.client.ISelectionChangeListener;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionChangedEvent;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.webclient.client.WebClientBundle;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Project;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Task;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.TaskService;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.TaskServiceAsync;

public class TaskList extends Composite implements ISelectionChangeListener {

	private static TaskListUiBinder uiBinder = GWT
			.create(TaskListUiBinder.class);

	interface TaskListUiBinder extends UiBinder<Widget, TaskList> {
	}

	private final TaskServiceAsync taskService = GWT.create(TaskService.class);

	@UiField(provided = true)
	WebClientBundle resources = WebClientBundle.INSTANCE;

	@UiField
	TaskDataTable taskList;

	@UiField
	PushButton newTask;

	@UiField
	PushButton editTask;

	@UiField
	PushButton deleteTask;

	public TaskList() {
		initWidget(uiBinder.createAndBindUi(this));

		SelectionManager.INSTANCE.addSelectionChangeListener(this);
	}

	public void selectionChanged(SelectionChangedEvent event) {

		if (event.getType() == SelectionChangedEvent.Type.PROJECT) {
			Project project = SelectionManager.INSTANCE.getSelectedProject();

			taskList.removeAllRows();
			if (project != null) {
				taskList.setWidget(0, 0, new LoadingLabel());

				taskService.getTasks(project.getId(),
						new AsyncCallback<List<Task>>() {

							public void onSuccess(List<Task> result) {
								taskList.removeAllRows();

								for (Task task : result) {
									taskList.addRow(task);
								}
							}

							public void onFailure(Throwable caught) {
							}
						});
			}
		}
	}

	@UiHandler("taskList")
	void onTableSelection(SelectionEvent<Task> event) {
		Task row = event.getSelectedItem();

		if (row != null)
			SelectionManager.INSTANCE.selectTask(row);
		else
			SelectionManager.INSTANCE.selectTask(null);
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

	public static class TaskDataTable extends SingleSelDataTable<Task> {
		@SuppressWarnings("unchecked")
		public TaskDataTable() {
			super(false, new TextDataTableColumn<Task>("Task", "100%") {
				@Override
				public String getCellText(Task row) {
					return row.getName();
				}
			});
			
			ContextMenu contextMenu = new ContextMenu();
			
			contextMenu.addItem("New Task", new Command() {
				public void execute() {
					System.out.println(">>> new Task");
				}
			});
			contextMenu.addItem("Edit Task", new Command() {
				public void execute() {
					System.out.println(">>> edit Task");
				}
			});
			
			setContextMenu(contextMenu);
		}
	}
}
