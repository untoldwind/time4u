package de.objectcode.time4u.server.ejb.seam.api.report;

public enum PersonProjection implements IProjection
{
  GIVEN_NAME(ColumnType.NAME, "Given name") {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getPerson().getGivenName() };
    }
  },
  SURNAME(ColumnType.NAME, "Surname") {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getPerson().getSurname() };
    }
  },
  NAME(ColumnType.NAME, "Name") {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getPerson().getGivenName() + " " + rowData.getPerson().getSurname() };
    }
  },
  EMAIL(ColumnType.NAME, "Email") {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getPerson().getEmail() };
    }
  };

  ColumnType m_columnType;
  String m_header;

  private PersonProjection(final ColumnType columnType, final String header)
  {
    m_columnType = columnType;
    m_header = header;
  }

  public ColumnDefinition getColumnDefinition(final int index)
  {
    return new ColumnDefinition(m_columnType, m_header, index);
  }
}
