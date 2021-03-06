package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.entities.ProjectEntity;

@XmlEnum
@XmlType(name = "project-projection")
public enum ProjectProjection implements IProjection
{
  ID(ColumnType.NAME, "Project id") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getProject() != null) {
        return rowData.getProject().getId();
      }
      return "";
    }
  },
  NAME(ColumnType.NAME, "Project") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getProject() != null) {
        return rowData.getProject().getName();
      }
      return "";
    }
  },
  PATH(ColumnType.NAME_ARRAY, "Project") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getProject() != null) {
        final List<String> names = new ArrayList<String>();

        ProjectEntity current = rowData.getProject();

        while (current != null) {
          names.add(0, current.getName());
          current = current.getParent();
        }
        return names;
      }
      return new String[0];
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

  public IAggregation createAggregation()
  {
    return null;
  }
}
