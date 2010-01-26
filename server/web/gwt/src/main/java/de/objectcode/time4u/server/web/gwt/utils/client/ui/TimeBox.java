package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

public class TimeBox extends Composite implements HasValue<Integer> {

	private Label label = new Label();
	private TextBox box = new TextBox();
	private PopupPanel popup;
	private Label timePicker = new Label("Placeholder");

	private int value;

	public TimeBox() {
		this(0);
	}

	public TimeBox(int value) {
		this.value = value;
		this.popup = new PopupPanel();

		popup.setAutoHideEnabled(true);
		popup.addAutoHidePartner(box.getElement());
		popup.setWidget(timePicker);
		popup.setStyleName("dateBoxPopup");

		initWidget(label);
		
		setValue(value);
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		setValue(value, false);
	}

	public void setValue(Integer value, boolean fireEvents) {
		if (value != this.value) {
			this.value = value;
			ValueChangeEvent.fire(this, value);
		}
		box.setText(TimeFormat.format(this.value));
		label.setText(TimeFormat.format(this.value));
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Integer> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

}
