package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ReportTableData extends ReportTableDataBase implements IsSerializable{
	/** Name of the report. */
	String m_name;

	public ReportTableData() {
	}
	

	public ReportTableData(final String name,
			final ReportColumnDefinition[] columns,
			final ReportColumnDefinition[] groupByColumns, String[] rows) {
		super(columns, groupByColumns, rows);

		m_name = name;
	}

	public String getName() {
		return m_name;
	}

}
