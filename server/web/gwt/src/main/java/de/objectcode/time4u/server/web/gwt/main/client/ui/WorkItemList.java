package de.objectcode.time4u.server.web.gwt.main.client.ui;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.main.client.ISelectionChangeListener;
import de.objectcode.time4u.server.web.gwt.main.client.MainClientBundle;
import de.objectcode.time4u.server.web.gwt.main.client.SelectionChangedEvent;
import de.objectcode.time4u.server.web.gwt.main.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.main.client.service.DayInfo;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItem;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItemService;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItemServiceAsync;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.DataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.DataTableRow;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLabel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TableHeader;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TimeBox;

public class WorkItemList extends Composite implements ISelectionChangeListener {

	private static WorkItemListUiBinder uiBinder = GWT
			.create(WorkItemListUiBinder.class);

	interface WorkItemListUiBinder extends UiBinder<Widget, WorkItemList> {
	}

	private final WorkItemServiceAsync workItemService = GWT
			.create(WorkItemService.class);

	@UiField(provided = true)
	MainClientBundle resources = MainClientBundle.INSTANCE;

	@UiField
	DataTable workItemList;

	public WorkItemList() {
		initWidget(uiBinder.createAndBindUi(this));

		SelectionManager.INSTANCE.addSelectionChangeListener(this);

		workItemList.setHeaders(new TableHeader("Begin", "4em"),
				new TableHeader("End", "4em"), new TableHeader("Project", "20%"),
				new TableHeader("Task", "20%"), new TableHeader("Comment"));
	}

	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getType() == SelectionChangedEvent.Type.DAY) {
			Date day = SelectionManager.INSTANCE.getSelectedDay();

			workItemList.removeAllRows();
			workItemList.setWidget(0, 0, new LoadingLabel());

			workItemService.getDayInfo(day, new AsyncCallback<DayInfo>() {
				public void onSuccess(DayInfo result) {
					workItemList.removeAllRows();

					if (result != null)
						for (WorkItem workItem : result.getWorkItems()) {
							workItemList.addRow(new DataTableRow(workItem,
									new TimeBox(workItem.getBegin()),
									new TimeBox(workItem.getEnd()), workItem
											.getProject().getName(), workItem
											.getTask().getName(), workItem
											.getComment()));
						}
				}

				public void onFailure(Throwable caught) {
				}
			});
		}
	}

}
