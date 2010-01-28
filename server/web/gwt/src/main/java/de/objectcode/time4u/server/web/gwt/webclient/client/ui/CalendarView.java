package de.objectcode.time4u.server.web.gwt.webclient.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;


import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.ExtendedSplitLayoutPanel;
import de.objectcode.time4u.server.web.gwt.webclient.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.DayInfoSummary;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.WorkItemService;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.WorkItemServiceAsync;

public class CalendarView extends Composite {
	private static CalendarViewUiBinder uiBinder = GWT
			.create(CalendarViewUiBinder.class);

	interface CalendarViewUiBinder extends UiBinder<Widget, CalendarView> {
	}

	private final WorkItemServiceAsync workItemService = GWT
			.create(WorkItemService.class);

	@UiField
	DatePicker calendar;

	public CalendarView() {
		initWidget(uiBinder.createAndBindUi(this));

		calendar.addShowRangeHandler(new ShowRangeHandler<Date>() {
			public void onShowRange(ShowRangeEvent<Date> event) {
				onCalendarShowRange(event);
			}
		});
		
		updateCalendar(calendar.getFirstDate(), calendar.getLastDate());
	}

	void onCalendarShowRange(ShowRangeEvent<Date> event) {
		updateCalendar(event.getStart(), event.getEnd());
	}

	void updateCalendar(Date start, Date end) {
		workItemService.getDayInfoSummaries(start, end,
				new AsyncCallback<List<DayInfoSummary>>() {
					public void onSuccess(List<DayInfoSummary> result) {
						for (DayInfoSummary dayInfo : result) {
							if (dayInfo.isHasWorkItems())
								calendar.addStyleToDates(
										"dayPickerDayIsHasWorkItems", dayInfo
												.getDay());
							if (dayInfo.isHasTags())
								calendar.addStyleToDates(
										"dayPickerDayIsHasTags", dayInfo
												.getDay());
							if (dayInfo.getCalculatedRegularTime() == 0)
								calendar.addStyleToDates(
										"dayPickerDayZeroRegular", dayInfo
												.getDay());
						}
					}

					public void onFailure(Throwable caught) {
					}
				});
	}

	@UiHandler("calendar")
	void onCalendarValueChange(ValueChangeEvent<Date> event) {
		SelectionManager.INSTANCE.selectedDate(event.getValue());
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
}
