package de.objectcode.time4u.server.ejb.seam.api.report;

public enum DayInfoProjection implements IProjection
{
  DATE(new ColumnDefinition(ColumnType.DATE, "Date")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getDayInfo().getDate() };
    }
  };

  private ColumnDefinition m_columnDefinition;

  private DayInfoProjection(final ColumnDefinition columnDefinition)
  {
    m_columnDefinition = columnDefinition;
  }

  public ColumnDefinition getColumnDefinition()
  {
    return m_columnDefinition;
  }

}
