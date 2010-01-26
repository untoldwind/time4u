package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.ArrayList;
import java.util.List;


public class ReportTableData extends ReportTableDataBase {
	/** Name of the report. */
	String m_name;

	public ReportTableData(final String name) {
		super(new ArrayList<ReportColumnDefinition>(),
				new ArrayList<ReportColumnDefinition>());

		m_name = name;
	}

	public ReportTableData(final String name,
			final List<ReportColumnDefinition> columns,
			final List<ReportColumnDefinition> groupByColumns) {
		super(columns, groupByColumns);

		m_name = name;
	}

	public String getName() {
		return m_name;
	}

}
