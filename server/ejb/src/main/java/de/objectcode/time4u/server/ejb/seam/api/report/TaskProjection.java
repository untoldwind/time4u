package de.objectcode.time4u.server.ejb.seam.api.report;

public enum TaskProjection implements IProjection
{
  NAME(ColumnType.NAME, "Name") {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getTask().getName() };
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
}