package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class ReportValue<T> implements IsSerializable {

	protected T value;

	public T getValue() {
		return value;
	}

	public static class StringReportValue extends ReportValue<String> {
		public StringReportValue() {
		}

		public StringReportValue(String value) {
			this.value = value;
		}
	}

	public static class StringArrayReportValue extends ReportValue<String[]> {

		public StringArrayReportValue() {
		}

		public StringArrayReportValue(List<String> value) {
			this.value = value.toArray(new String[value.size()]);
		}

		public StringArrayReportValue(String[] value) {
			this.value = value;
		}
	}

	public static class IntegerReportValue extends ReportValue<Integer> {
		public IntegerReportValue() {
		}

		public IntegerReportValue(int value) {
			this.value = value;
		}
	}

	public static class DateReportValue extends ReportValue<Date> {
		public DateReportValue() {
		}

		public DateReportValue(Date value) {
			this.value = value;
		}
	}
}
