package de.objectcode.time4u.server.web.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;


public class TaskList extends Composite {

	private static TaskListUiBinder uiBinder = GWT
			.create(TaskListUiBinder.class);

	interface TaskListUiBinder extends UiBinder<Widget, TaskList> {
	}

	@UiField
	ListBox taskList;

	public TaskList() {
		initWidget(uiBinder.createAndBindUi(this));

	}
}
