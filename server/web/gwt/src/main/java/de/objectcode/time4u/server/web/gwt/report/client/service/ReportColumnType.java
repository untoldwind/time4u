package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.TimeFormat;

public enum ReportColumnType {
	/** A name is a short text like a project name, task name, user name, ... */
	NAME() {
		@Override
		public String formatValue(String value) {
			return value;
		}
	},
	/** An array of names (to be displayed in a single column) */
	NAME_ARRAY() {
		@Override
		public String formatValue(String value) {
			return value;
		}
	},
	/**
	 * A description is a long text like a project description, workitem
	 * comment, ...
	 */
	DESCRIPTION() {
		@Override
		public String formatValue(String value) {
			return value;
		}
	},
	/** An integer value */
	INTEGER() {
		@Override
		public String formatValue(String value) {
			return value;
		}

	},
	/** Time of day 00:00:00 to 24:00:00 */
	TIME() {
		@Override
		public String formatValue(String value) {
			return TimeFormat.format(Integer.parseInt(value));
		}

	},
	/** A date */
	DATE() {
		@Override
		public String formatValue(String value) {
			return DateTimeFormat.getMediumDateFormat().format(new Date(Long.parseLong(value)));
		}

	},
	/** A timestamp */
	TIMESTAMP() {
		@Override
		public String formatValue(String value) {
			return DateTimeFormat.getMediumDateTimeFormat().format(new Date(Long.parseLong(value)));
		}
	};

	public abstract String formatValue(String value);
}
