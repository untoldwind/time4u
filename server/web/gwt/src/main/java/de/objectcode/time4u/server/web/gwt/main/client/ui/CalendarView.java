package de.objectcode.time4u.server.web.gwt.main.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.objectcode.time4u.server.web.gwt.main.client.SelectionManager;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItemService;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItemServiceAsync;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.objectcode.time4u.server.web.gwt.main.client.service.DayInfoSummary;

public class CalendarView extends Composite {
	private static CalendarViewUiBinder uiBinder = GWT
			.create(CalendarViewUiBinder.class);

	interface CalendarViewUiBinder extends UiBinder<Widget, CalendarView> {
	}

	private final WorkItemServiceAsync workItemService = GWT
			.create(WorkItemService.class);

	@UiField
	DatePicker calendar;

	public CalendarView(SelectionManager selectionManager) {
		initWidget(uiBinder.createAndBindUi(this));

		calendar.addShowRangeHandlerAndFire(new ShowRangeHandler<Date>() {
			public void onShowRange(ShowRangeEvent<Date> event) {
				onCalendarShowRange(event);
			}
		});
	}

	void onCalendarShowRange(ShowRangeEvent<Date> event) {
		workItemService.getDayInfoSummaries(event.getStart(), event.getEnd(),
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
							if ( dayInfo.getRegularTime() == 0 )
								calendar.addStyleToDates(
										"dayPickerDayZeroRegular", dayInfo
												.getDay());
						}
					}

					public void onFailure(Throwable caught) {
					}
				});
	}
}
