package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReportTableGroupData extends ReportTableDataBase implements
		IsSerializable {
	String m_id;
	String m_label;

	public ReportTableGroupData() {
	}

	public ReportTableGroupData(final IdLabelPair valueLabel,
			final List<ReportColumnDefinition> columns,
			final List<ReportColumnDefinition> groupByColumns) {
		super(columns, groupByColumns);

		m_id = valueLabel.getId();
		m_label = valueLabel.getLabel();
	}

	public String getId() {
		return m_id;
	}

	public String getLabel() {
		return m_label;
	}

}
