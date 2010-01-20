package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLabel;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.TimeBox;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.DataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable.TextDataTableColumn;
import de.objectcode.time4u.server.web.gwt.webclient.client.ISelectionChangeListener;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionChangedEvent;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.webclient.client.WebClientBundle;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.DayInfo;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.WorkItem;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.WorkItemService;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.WorkItemServiceAsync;

public class WorkItemList extends Composite implements ISelectionChangeListener {

	private static WorkItemListUiBinder uiBinder = GWT
			.create(WorkItemListUiBinder.class);

	interface WorkItemListUiBinder extends UiBinder<Widget, WorkItemList> {
	}

	private final WorkItemServiceAsync workItemService = GWT
			.create(WorkItemService.class);

	@UiField(provided = true)
	WebClientBundle resources = WebClientBundle.INSTANCE;

	@UiField
	WorkItemDataTable workItemList;

	public WorkItemList() {
		initWidget(uiBinder.createAndBindUi(this));

		SelectionManager.INSTANCE.addSelectionChangeListener(this);

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
							workItemList.addRow(workItem);
						}
				}

				public void onFailure(Throwable caught) {
				}
			});
		}
	}

	public static class WorkItemDataTable extends DataTable<WorkItem> {
		@SuppressWarnings("unchecked")
		public WorkItemDataTable() {
			super(new TextDataTableColumn<WorkItem>("Begin", "4em") {
				@Override
				public String getCellText(WorkItem row) {
					return TimeBox.TimeFormat.format(row.getBegin());
				}
			}, new TextDataTableColumn<WorkItem>("End", "4em") {
				@Override
				public String getCellText(WorkItem row) {
					return TimeBox.TimeFormat.format(row.getEnd());
				}
			}, new TextDataTableColumn<WorkItem>("Project", "20%") {
				@Override
				public String getCellText(WorkItem row) {
					return row.getProject().getName();
				}
			}, new TextDataTableColumn<WorkItem>("Task", "20%") {
				@Override
				public String getCellText(WorkItem row) {
					return row.getTask().getName();
				}
			}, new TextDataTableColumn<WorkItem>("Comment", null) {
				@Override
				public String getCellText(WorkItem row) {
					return row.getComment();
				}
			});
		}

	}
}
