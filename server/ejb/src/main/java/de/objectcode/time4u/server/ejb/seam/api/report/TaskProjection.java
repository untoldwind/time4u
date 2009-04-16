package de.objectcode.time4u.server.ejb.seam.api.report;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "task-projection")
public enum TaskProjection implements IProjection
{
  NAME(ColumnType.NAME, "Task") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTask() != null) {
        return rowData.getTask().getName();
      }
      return "";
    }
  };

  ColumnType m_columnType;
  String m_header;

  private TaskProjection(final ColumnType columnType, final String header)
  {
    m_columnType = columnType;
    m_header = header;
  }

  public ColumnDefinition getColumnDefinition(final int index)
  {
    return new ColumnDefinition(m_columnType, m_header, index);
  }

  public IAggregation createAggregation()
  {
    return null;
  }
}