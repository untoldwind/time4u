package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.entities.TodoAssignmentEntity;
import de.objectcode.time4u.server.entities.TodoGroupEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

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
  /** Project the id of a todo to a report column. */
  ID(ColumnType.NAME, "Todo id") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getId();
      }
      return "";
    }
  },
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
  },
  /** Project the createdAt of a todo to a report column. */
  CREATED_AT(ColumnType.TIMESTAMP, "Created at") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getCreatedAt();
      }
      return null;
    }
  },
  /** Project the completedAt of a todo to a report column. */
  COMPLETED_AT(ColumnType.TIMESTAMP, "Created at") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getCompletedAt();
      }
      return null;
    }
  },
  /** Project the deadline of a todo to a report column. */
  DEADLINE(ColumnType.TIMESTAMP, "Deadline") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getDeadline();
      }
      return "";
    }
  },
  /** Project the state of a todo to a report column. */
  STATE(ColumnType.NAME, "State") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getState().toString();
      }
      return "";
    }
  },
  /** Priority of the todo */
  PRIORITY(ColumnType.INTEGER, "Priority") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getPriority();
      }
      return "";
    }
  },
  /** Estimated effort by reporter. */
  ESTIMATED_REPORTER(ColumnType.TIME, "Estimated (Reporter)") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        return rowData.getTodo().getEstimatedTime();
      }
      return null;
    }
  },
  /** Sum of estimated efforts by assigned persons. */
  ESTIMATED_ASSIGNEES(ColumnType.TIME, "Estimated (Assignees)") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        int estimatedTime = 0;
        for (final TodoAssignmentEntity assignement : rowData.getTodo().getAssignments().values()) {
          if (assignement.getEstimatedTime() != null) {
            estimatedTime += assignement.getEstimatedTime();
          }
        }
        return estimatedTime;
      }
      return null;
    }
  },
  /** Sum of all durations associated with this todo. */
  SUM_DURATIONS(ColumnType.TIME, "Sum durations") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getTodo() != null) {
        int sumDuration = 0;
        for (final WorkItemEntity workItem : rowData.getTodo().getWorkItems()) {
          sumDuration += workItem.getDuration();
        }
        return sumDuration;
      }
      return null;
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
