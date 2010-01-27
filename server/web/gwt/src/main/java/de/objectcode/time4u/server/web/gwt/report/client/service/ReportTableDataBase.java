package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReportTableDataBase implements IsSerializable {
	/** List of all column definitions. */
	protected ReportColumnDefinition[] m_columns;
	/** List of all column definitions that have been used for group by. */
	protected ReportColumnDefinition[] m_groupByColumns;
	/** List of report rows if there are no group-by. */
	protected String[] m_rows;
	/** Aggregated values to be displayed in the footer. */
	protected String[] m_aggregates;
	/** Map of group-by sub-reports. */
	protected ReportTableGroupData[] m_groups;

	protected ReportTableDataBase() {
	}

	protected ReportTableDataBase(final ReportColumnDefinition[] columns,
			final ReportColumnDefinition[] groupByColumns, final String[] rows) {
		m_columns = columns;
		m_groupByColumns = groupByColumns;
		m_rows = rows;
		m_groups = null;
	}

	public ReportColumnDefinition[] getColumns() {
		return m_columns;
	}

	public ReportColumnDefinition[] getGroupByColumns() {
		return m_groupByColumns;
	}

	public String[] getRows() {
		return m_rows;
	}

	public boolean isHasRows() {
		return m_rows.length > 0;
	}

	public boolean isHasGroups() {
		return m_groupByColumns.length > 0;
	}

	public boolean isHasAggregates() {
		return m_aggregates != null;
	}

	public String[] getAggregates() {
		return m_aggregates;
	}

	public ReportTableGroupData[] getGroups() {
		return m_groups;
	}
}
