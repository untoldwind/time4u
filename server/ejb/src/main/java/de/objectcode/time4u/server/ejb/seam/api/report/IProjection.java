package de.objectcode.time4u.server.ejb.seam.api.report;

/**
 * Generic interface of a data projection.
 * 
 * A projection projects the data of an entity to a report column.
 * 
 * @author junglas
 */
public interface IProjection
{
  /**
   * Get the column definition.
   * 
   * @param index
   *          The column index
   * @return The column definition
   */
  ColumnDefinition getColumnDefinition(int index);

  /**
   * Perform the projection.
   * 
   * @param rowData
   *          The row data referencing the entity to project
   * @return The projected value
   */
  Object project(IRowDataAdapter rowData);

  /**
   * Create a standard aggregation of the projection.
   * 
   * E.g. usually the workitem durations a summed up at the end of a report.
   * 
   * @return The standard aggregation or <tt>null</tt> if the values should not be aggregated
   */
  IAggregation createAggregation();
}
