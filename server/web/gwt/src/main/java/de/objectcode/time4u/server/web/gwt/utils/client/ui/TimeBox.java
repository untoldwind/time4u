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

	public static class TimeFormat {
		public static int parse(final String str) {
			final int idx = str.indexOf(':');

			if (idx < 0) {
				final int hour = Integer.parseInt(str);

				return hour * 3600;
			}

			final int hour = Integer.parseInt(str.substring(0, idx));
			final int minute = Integer.parseInt(str.substring(idx + 1));

			return 60 * (hour * 60 + minute);
		}

		public static String formatFull(int time) {
			final StringBuffer buffer = new StringBuffer();
			time /= 60;

			if (time < 0) {
				buffer.append('-');
				time = -time;
			}
			buffer.append(time / 60);
			buffer.append(':');
			if (time % 60 < 10) {
				buffer.append('0');
			}
			buffer.append(time % 60);
			return buffer.toString();
		}

		public static String format(int time) {
			final char[] ch = new char[5];

			time /= 60;

			ch[4] = Character.forDigit(time % 10, 10);
			time /= 10;
			ch[3] = Character.forDigit(time % 6, 10);
			time /= 6;
			ch[2] = ':';
			ch[1] = Character.forDigit(time % 10, 10);
			time /= 10;
			ch[0] = Character.forDigit(time % 10, 10);

			return new String(ch);
		}
	}

}
