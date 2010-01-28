package de.objectcode.time4u.server.web.gwt.report.server.dao.jpa;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.objectcode.time4u.server.entities.IdAndNameAwareEntity;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableData;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableColumnType;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRow;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRowType;
import de.objectcode.time4u.server.web.gwt.report.client.service.IdLabelPair;

public class GenericCrosstableDataCollector<ColumnEntity extends IdAndNameAwareEntity, RowEntity extends IdAndNameAwareEntity> {
	protected final SortedSet<ColumnEntity> m_sortedColumnEntities;
	protected final SortedSet<RowEntity> m_sortedRowEntities;
	protected final HashMap<ColumnEntity, HashMap<RowEntity, Integer>> m_dataMap;

	public GenericCrosstableDataCollector(
			final Comparator<ColumnEntity> columnEntityComparator,
			final Comparator<RowEntity> rowEntityComparator) {
		m_sortedColumnEntities = new TreeSet<ColumnEntity>(
				columnEntityComparator);
		m_sortedRowEntities = new TreeSet<RowEntity>(rowEntityComparator);

		m_dataMap = new HashMap<ColumnEntity, HashMap<RowEntity, Integer>>();
	}

	public void collect(final RowDataAdaptor<ColumnEntity, RowEntity> rowData) {
		m_sortedColumnEntities.add(rowData.getColumnEntity());
		m_sortedRowEntities.add(rowData.getRowEntity());

		HashMap<RowEntity, Integer> dataSubMap = m_dataMap.get(rowData
				.getColumnEntity());
		if (dataSubMap == null) {
			dataSubMap = new HashMap<RowEntity, Integer>();
			dataSubMap.put(rowData.getRowEntity(), new Integer(0));
			m_dataMap.put(rowData.getColumnEntity(), dataSubMap);
		}

		final int oldValue = dataSubMap.get(rowData.getRowEntity()) == null ? 0
				: dataSubMap.get(rowData.getRowEntity());
		dataSubMap.put(rowData.getRowEntity(), new Integer(oldValue
				+ rowData.getAggregate()));
	}

	public static class RowDataAdaptor<ColumnEntity, RowEntity> {
		protected ColumnEntity columnEntity;
		protected RowEntity rowEntity;
		private final Integer aggregate;

		public ColumnEntity getColumnEntity() {
			return columnEntity;
		}

		public RowEntity getRowEntity() {
			return rowEntity;
		}

		public Integer getAggregate() {
			return aggregate;
		}

		public RowDataAdaptor(final ColumnEntity columnEntity,
				final RowEntity rowEntity, final Integer aggregate) {
			super();
			this.columnEntity = columnEntity;
			this.rowEntity = rowEntity;
			this.aggregate = aggregate;
		}
	}

	public void finish() {

	}

	public CrossTableData getCrossTable(CrossTableColumnType columnType, CrossTableRowType rowType) {
		final IdLabelPair columns[] = new IdLabelPair[m_sortedColumnEntities
				.size()];
		int columnCount = 0;

		for (final ColumnEntity columnEntity : m_sortedColumnEntities) {
			columns[columnCount++] = new IdLabelPair(columnEntity.getId(),
					getColumnLabel(columnEntity));
		}

		final CrossTableRow[] rows = new CrossTableRow[m_sortedRowEntities
				.size()];
		int rowCount = 0;

		final int[] columnAggregates = new int[columns.length];
		int totalAggregate = 0;

		for (final RowEntity rowEntity : m_sortedRowEntities) {
			final IdLabelPair rowHeader = new IdLabelPair(rowEntity.getId(),
					getRowLabel(rowEntity));
			final int[] data = new int[columns.length];
			int i = 0;
			int rowAggregate = 0;

			for (final ColumnEntity columnEntity : m_sortedColumnEntities) {
				final Map<RowEntity, Integer> dataSubMap = m_dataMap
						.get(columnEntity);

				if (dataSubMap != null) {
					final Integer value = dataSubMap.get(rowEntity);
					data[i] = value != null ? value : 0;
					rowAggregate += value != null ? value.intValue() : 0;
					columnAggregates[i] = columnAggregates[i]
							+ (value != null ? value.intValue() : 0);
					totalAggregate += value != null ? value.intValue() : 0;
				}

				i++;
			}

			rows[rowCount] = new CrossTableRow(rowHeader, data, rowAggregate);
			rowCount++;
		}

		return new CrossTableData(columnType, rowType, columns, rows, columnAggregates, totalAggregate);
	}

	protected String getColumnLabel(final ColumnEntity columnEntity) {
		return columnEntity.getName();
	}

	protected String getRowLabel(final RowEntity rowEntity) {
		return rowEntity.getName();
	}

}
