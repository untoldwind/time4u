package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class ReportValue implements IsSerializable {

	public static class StringReportValue extends ReportValue {
		String value;

		public StringReportValue() {
		}

		public StringReportValue(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static class TimeReportValue extends ReportValue {
		int value;

		public TimeReportValue() {
		}

		public TimeReportValue(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
