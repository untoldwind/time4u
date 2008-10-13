package de.objectcode.time4u.server.ejb.seam.api.report;

public interface IProjection
{
  ColumnDefinition getColumnDefinition();

  Object[] project(IRowDataAdapter rowData);
}
