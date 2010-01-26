package de.objectcode.time4u.server.web.gwt.report.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.objectcode.time4u.server.ejb.seam.api.report.ColumnType;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportRow;

public class ReportTableDataBase implements IsSerializable {
	/** List of all column definitions. */
	protected List<ReportColumnDefinition> m_columns;
	/** List of all column definitions that have been used for group by. */
	protected List<ReportColumnDefinition> m_groupByColumns;
	/** List of report rows if there are no group-by. */
	protected List<ReportRow> m_rows;
	/** Aggregated values to be displayed in the footer. */
	protected int[] m_aggregates;
	/** Map of group-by sub-reports. */
	protected Map<String, ReportTableGroupData> m_groups;

	protected ReportTableDataBase(final List<ReportColumnDefinition> columns,
			final List<ReportColumnDefinition> groupByColumns) {
		m_columns = columns;
		m_groupByColumns = groupByColumns;
		m_rows = new ArrayList<ReportRow>();
		m_groups = new TreeMap<String, ReportTableGroupData>();
	}

	public void addColumn(final ColumnType columnType, final String header) {
		m_columns
				.add(new ReportColumnDefinition(columnType, header, m_columns.size()));
	}

	public List<ReportColumnDefinition> getColumns() {
		return m_columns;
	}

	public void addGroupByColumn(final ColumnType columnType,
			final String header) {
		m_groupByColumns.add(new ReportColumnDefinition(columnType, header,
				m_groupByColumns.size()));
	}

	public List<ReportColumnDefinition> getGroupByColumns() {
		return m_groupByColumns;
	}

	public List<ReportRow> getRows() {
		return m_rows;
	}

	public boolean isHasRows() {
		return m_rows != null && m_rows.size() > 0;
	}

	public boolean isHasGroups() {
		return !m_groupByColumns.isEmpty();
	}

	public boolean isHasAggregates() {
		return m_aggregates != null;
	}

	public int[] getAggregates() {
		return m_aggregates;
	}

	public void setAggregates(final List<Object> groupKey,
			final int[] aggregates) {
		if (groupKey == null) {
			m_aggregates = aggregates;
		} else {
			ReportTableDataBase current = this;

			for (final Object key : groupKey) {
				if (current == null) {
					break;
				}
				current = current.m_groups.get(key);
			}

			if (current != null) {
				current.m_aggregates = aggregates;
			}
		}
	}

	public List<ReportTableGroupData> getGroups() {
		return new ArrayList<ReportTableGroupData>(m_groups.values());
	}

	public void addRow(final List<IdLabelPair> groups, final Object[] row) {
		addRow(0, groups, row);
	}

	protected void addRow(final int depth, final List<IdLabelPair> groups,
			final Object[] row) {
		if (depth >= groups.size()) {
			if (row != null) {
				m_rows.add(new ReportRow(m_rows.size(), row));
			}
		} else {
			final IdLabelPair top = groups.get(depth);
			ReportTableGroupData group = m_groups.get(top.getId());

			if (group == null) {
				final List<ReportColumnDefinition> groupByColumns = new ArrayList<ReportColumnDefinition>();

				if (m_groupByColumns.size() > 1) {
					groupByColumns.addAll(m_groupByColumns.subList(1,
							m_groupByColumns.size()));
				}
				group = new ReportTableGroupData(top, m_columns, groupByColumns);

				m_groups.put(group.getId(), group);
			}

			if (row != null) {
				group.addRow(depth + 1, groups, row);
			}
		}
	}

}
