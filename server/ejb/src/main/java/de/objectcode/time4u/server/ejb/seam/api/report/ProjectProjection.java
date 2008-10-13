package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.entities.ProjectEntity;

public enum ProjectProjection implements IProjection
{
  NAME(ColumnType.NAME, "Project") {
    public Object[] project(final IRowDataAdapter rowData)
    {
      return new Object[] { rowData.getProject().getName() };
    }
  },
  PATH(ColumnType.NAME, "Project") {
    public Object[] project(final IRowDataAdapter rowData)
    {
      final List<String> names = new ArrayList<String>();

      ProjectEntity current = rowData.getProject();

      while (current != null) {
        names.add(0, current.getName());
        current = current.getParent();
      }
      return names.toArray();
    }
  };

  ColumnType m_columnType;
  String m_header;

  private ProjectProjection(final ColumnType columnType, final String header)
  {
    m_columnType = columnType;
    m_header = header;
  }

  public ColumnDefinition getColumnDefinition(final int index)
  {
    return new ColumnDefinition(m_columnType, m_header, index);
  }

}
