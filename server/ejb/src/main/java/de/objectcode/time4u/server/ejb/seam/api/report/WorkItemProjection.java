package de.objectcode.time4u.server.ejb.seam.api.report;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Enumeration of all workitem projections.
 * 
 * These projections only work in context of a workitem.
 * 
 * @author junglas
 */
@XmlEnum
@XmlType(name = "workitem-projection")
public enum WorkItemProjection implements IProjection
{
  /** Project the workitem comment to a report column. */
  COMMENT(ColumnType.DESCRIPTION, "Comment") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getWorkItem().getComment();
    }
  },
  /** Project the workitem begin time to a report column. */
  BEGIN(ColumnType.TIME, "Begin") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getWorkItem().getBegin();
    }
  },
  /** Project the workitem end time to a report column. */
  END(ColumnType.TIME, "End") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getWorkItem().getEnd();
    }
  },
  /** Project the workitem duration to a report column. */
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

  /**
   * {@inheritDoc}
   */
  public ColumnDefinition getColumnDefinition(final int index)
  {
    return new ColumnDefinition(m_columnType, m_header, index);
  }

  /**
   * {@inheritDoc}
   */
  public IAggregation createAggregation()
  {
    return null;
  }
}
