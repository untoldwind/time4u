package de.objectcode.time4u.server.web.gwt.main.client.ui;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.main.client.ISelectionChangeListener;
import de.objectcode.time4u.server.web.gwt.main.client.SelectionChangedEvent;
import de.objectcode.time4u.server.web.gwt.main.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.main.client.service.DayInfo;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItem;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItemService;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItemServiceAsync;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.DataTable;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.DataTableRow;
import de.objectcode.time4u.server.web.gwt.utils.client.ui.LoadingLabel;

public class WorkItemList extends Composite implements ISelectionChangeListener {

	private static WorkItemListUiBinder uiBinder = GWT
			.create(WorkItemListUiBinder.class);

	interface WorkItemListUiBinder extends UiBinder<Widget, WorkItemList> {
	}

	private final WorkItemServiceAsync workItemService = GWT
			.create(WorkItemService.class);

	@UiField
	DataTable workItemList;

	SelectionManager selectionManager;

	public WorkItemList(SelectionManager selectionManager) {
		initWidget(uiBinder.createAndBindUi(this));

		this.selectionManager = selectionManager;
		this.selectionManager.addSelectionChangeListener(this);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getType() == SelectionChangedEvent.Type.DAY) {
			Date day = selectionManager.getSelectedDay();

			workItemList.removeAllRows();
			workItemList.setWidget(0, 0, new LoadingLabel());

			workItemService.getDayInfo(day, new AsyncCallback<DayInfo>() {				
				public void onSuccess(DayInfo result) {
					workItemList.removeAllRows();

					for (WorkItem workItem : result.getWorkItems()) {
						workItemList.addRow(new DataTableRow(workItem, workItem.getBegin(), workItem.getEnd(), workItem.getComment()));
					}
				}
				
				public void onFailure(Throwable caught) {
				}
			});
		}
	}

}
