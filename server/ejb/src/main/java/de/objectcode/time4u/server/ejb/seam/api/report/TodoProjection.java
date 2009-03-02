package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.entities.TodoGroupEntity;

/**
 * Enumeration of all todo projections.
 * 
 * These projectios only work in context of a todo (or an entity that is associated with a todo).
 * 
 * @author junglas
 */
@XmlEnum
@XmlType(name = "todo-projection")
public enum TodoProjection implements IProjection
{
  /** Project the header of a todo to a report column. */
  HEADER(ColumnType.NAME, "Todo header") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getHeader();
      }
      return "";
    }
  },
  /** Project the description of a todo to a report column. */
  DESCRIPTION(ColumnType.DESCRIPTION, "Todo description") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getDescription();
      }
      return "";
    }
  },
  /** Project all groups of a todo to a report column. */
  GROUPS(ColumnType.NAME_ARRAY, "Todo groups") {
    public Object project(final IRowDataAdapter rowData)
    {
      final List<String> headers = new ArrayList<String>();

      if (rowData.getTodo() != null) {
        TodoGroupEntity current = rowData.getTodo().getGroup();

        while (current != null) {
          headers.add(0, current.getHeader());
          current = current.getGroup();
        }
      }
      return headers;
    }
  };

  ColumnType m_columnType;
  String m_header;

  private TodoProjection(final ColumnType columnType, final String header)
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
