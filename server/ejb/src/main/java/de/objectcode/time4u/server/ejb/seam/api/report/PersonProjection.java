package de.objectcode.time4u.server.ejb.seam.api.report;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "person-projection")
public enum PersonProjection implements IProjection
{
  ID(ColumnType.NAME, "Person id") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getPerson().getId();
    }
  },
  GIVEN_NAME(ColumnType.NAME, "Given name") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getPerson().getGivenName();
    }
  },
  SURNAME(ColumnType.NAME, "Surname") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getPerson().getSurname();
    }
  },
  NAME(ColumnType.NAME, "Person") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getPerson().getGivenName() + " " + rowData.getPerson().getSurname();
    }
  },
  EMAIL(ColumnType.NAME, "Email") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getPerson().getEmail();
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

  public IAggregation createAggregation()
  {
    return null;
  }
}
