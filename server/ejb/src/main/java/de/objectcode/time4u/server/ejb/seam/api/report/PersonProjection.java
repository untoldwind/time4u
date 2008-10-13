package de.objectcode.time4u.server.ejb.seam.api.report;

public enum PersonProjection implements IProjection
{
  GIVEN_NAME(new ColumnDefinition(ColumnType.NAME, "Given name")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getPerson().getGivenName() };
    }
  },
  SURNAME(new ColumnDefinition(ColumnType.NAME, "Surname")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getPerson().getSurname() };
    }
  },
  NAME(new ColumnDefinition(ColumnType.NAME, "Name")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getPerson().getGivenName() + " " + rowData.getPerson().getSurname() };
    }
  },
  EMAIL(new ColumnDefinition(ColumnType.NAME, "Email")) {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getPerson().getEmail() };
    }
  };

  private ColumnDefinition m_columnDefinition;

  private PersonProjection(final ColumnDefinition columnDefinition)
  {
    m_columnDefinition = columnDefinition;
  }

  public ColumnDefinition getColumnDefinition()
  {
    return m_columnDefinition;
  }
}
