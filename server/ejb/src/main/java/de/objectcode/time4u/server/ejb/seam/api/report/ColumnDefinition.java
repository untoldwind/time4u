package de.objectcode.time4u.server.ejb.seam.api.report;

public class ColumnDefinition
{
  ColumnType m_columnType;
  String m_header;

  public ColumnDefinition(final ColumnType columnType, final String header)
  {
    m_columnType = columnType;
    m_header = header;
  }

  public ColumnType getColumnType()
  {
    return m_columnType;
  }

  public void setColumnType(final ColumnType columnType)
  {
    m_columnType = columnType;
  }

  public String getHeader()
  {
    return m_header;
  }

  public void setHeader(final String header)
  {
    m_header = header;
  }

}
