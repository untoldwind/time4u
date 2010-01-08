package de.objectcode.time4u.server.web.gwt.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class CalendarView extends Composite {
	private static CalendarViewUiBinder uiBinder = GWT
			.create(CalendarViewUiBinder.class);

	interface CalendarViewUiBinder extends UiBinder<Widget, CalendarView> {
	}

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
		System.out.println(">>> " + event.getStart() + " " + event.getEnd());
	}
}
