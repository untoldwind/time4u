package de.objectcode.time4u.server.ejb.seam.api.report;

public enum WorkItemProjection implements IProjection
{
  COMMENT(new ColumnDefinition(ColumnType.DESCRIPTION, "Comment")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getWorkItem().getComment() };
    }
  },
  BEGIN(new ColumnDefinition(ColumnType.TIME, "Begin")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getWorkItem().getBegin() };
    }
  },
  END(new ColumnDefinition(ColumnType.TIME, "End")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getWorkItem().getEnd() };
    }
  },
  DURATION(new ColumnDefinition(ColumnType.TIME, "Duration")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getWorkItem().getEnd() - rowData.getWorkItem().getBegin() };
    }
  };

  private ColumnDefinition m_columnDefinition;

  private WorkItemProjection(final ColumnDefinition columnDefinition)
  {
    m_columnDefinition = columnDefinition;
  }

  public ColumnDefinition getColumnDefinition()
  {
    return m_columnDefinition;
  }
}
