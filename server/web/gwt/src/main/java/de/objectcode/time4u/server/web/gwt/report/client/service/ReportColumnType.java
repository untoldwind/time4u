package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.Arrays;

import com.google.gwt.i18n.client.DateTimeFormat;

import de.objectcode.time4u.server.web.gwt.utils.client.ui.TimeFormat;

public enum ReportColumnType {
	/** A name is a short text like a project name, task name, user name, ... */
	NAME() {
		@Override
		public String formatValue(ReportValue<?> value) {
			return ((ReportValue.StringReportValue) value).getValue();
		}
	},
	/** An array of names (to be displayed in a single column) */
	NAME_ARRAY() {
		@Override
		public String formatValue(ReportValue<?> value) {
			return ""; //Arrays.toString(((ReportValue.StringArrayReportValue)data[i]).getValue());
		}
	},
	/**
	 * A description is a long text like a project description, workitem
	 * comment, ...
	 */
	DESCRIPTION() {
		@Override
		public String formatValue(ReportValue<?> value) {
			return ((ReportValue.StringReportValue) value).getValue();
		}
	},
	/** An integer value */
	INTEGER() {
		@Override
		public String formatValue(ReportValue<?> value) {
			return String.valueOf(((ReportValue.IntegerReportValue) value).getValue());
		}

	},
	/** Time of day 00:00:00 to 24:00:00 */
	TIME() {
		@Override
		public String formatValue(ReportValue<?> value) {
			return TimeFormat.format(((ReportValue.IntegerReportValue) value).getValue());
		}

	},
	/** A date */
	DATE() {
		@Override
		public String formatValue(ReportValue<?> value) {
			return DateTimeFormat.getMediumDateFormat().format(
					((ReportValue.DateReportValue) value).getValue());
		}

	},
	/** A timestamp */
	TIMESTAMP() {
		@Override
		public String formatValue(ReportValue<?> value) {
			return DateTimeFormat.getMediumDateTimeFormat().format(
					((ReportValue.DateReportValue) value).getValue());
		}
	};

	public abstract String formatValue(ReportValue<?> value);
}
