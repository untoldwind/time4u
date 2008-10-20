package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.entities.DayTagEntity;

public enum DayInfoProjection implements IProjection
{
  DATE(ColumnType.DATE, "Date") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getDayInfo().getDate();
    }
  },
  SUM_DURATIONS(ColumnType.TIME, "Sum durations") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getDayInfo().getSumDurations();
    }
  },
  TAGS(ColumnType.NAME_ARRAY, "Tags") {
    public Object project(final IRowDataAdapter rowData)
    {
      final List<String> tags = new ArrayList<String>();

      for (final DayTagEntity dayTag : rowData.getDayInfo().getTags()) {
        tags.add(dayTag.getName());
      }
      return tags;
    }
  };

  ColumnType m_columnType;
  String m_header;

  private DayInfoProjection(final ColumnType columnType, final String header)
  {
    m_columnType = columnType;
    m_header = header;
  }

  public ColumnDefinition getColumnDefinition(final int index)
  {
    return new ColumnDefinition(m_columnType, m_header, index);
  }

}
