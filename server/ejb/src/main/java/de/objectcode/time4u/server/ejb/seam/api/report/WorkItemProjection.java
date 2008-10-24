package de.objectcode.time4u.server.ejb.seam.api.report;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "workitem-projection")
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

    @Override
    public IAggregation createAggregation()
    {
      return new SumAggregation();
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

  public IAggregation createAggregation()
  {
    return null;
  }
}
