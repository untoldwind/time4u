package de.objectcode.time4u.server.ejb.seam.api.report;

public enum WorkItemProjection implements IProjection
{
  COMMENT(ColumnType.DESCRIPTION, "Comment") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getWorkItem().getComment();
    }
  },
  BEGIN(ColumnType.TIME, "Begin") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getWorkItem().getBegin();
    }
  },
  END(ColumnType.TIME, "End") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getWorkItem().getEnd();
    }
  },
  DURATION(ColumnType.TIME, "Duration") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getWorkItem().getEnd() - rowData.getWorkItem().getBegin();
    }
  };
  ColumnType m_columnType;
  String m_header;

  private WorkItemProjection(final ColumnType columnType, final String header)
  {
    m_columnType = columnType;
    m_header = header;
  }

  public ColumnDefinition getColumnDefinition(final int index)
  {
    return new ColumnDefinition(m_columnType, m_header, index);
  }
}
