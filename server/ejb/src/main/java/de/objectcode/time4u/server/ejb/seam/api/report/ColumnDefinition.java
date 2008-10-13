package de.objectcode.time4u.server.ejb.seam.api.report;

public class ColumnDefinition
{
  ColumnType m_columnType;
  String m_header;
  int m_index;

  public ColumnDefinition(final ColumnType columnType, final String header, final int index)
  {
    m_columnType = columnType;
    m_header = header;
    m_index = index;
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

  public int getIndex()
  {
    return m_index;
  }

  public void setIndex(final int index)
  {
    m_index = index;
  }

}
