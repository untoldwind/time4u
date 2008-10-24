package de.objectcode.time4u.server.ejb.seam.api.report;

public interface IProjection
{
  ColumnDefinition getColumnDefinition(int index);

  Object project(IRowDataAdapter rowData);

  IAggregation createAggregation();
}
