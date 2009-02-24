package de.objectcode.time4u.server.ejb.seam.api.report;

/**
 * Generic interface of an aggregation.
 * 
 * @author junglas
 */
public interface IAggregation
{
  /**
   * Collect a value (after projection).
   * 
   * @param value
   *          The value to collection
   */
  void collect(Object value);

  /**
   * Finish the aggregation.
   * 
   * This method is called after all values have been collected.
   */
  void finish();

  /**
   * Get the aggregated value.
   * 
   * @return The aggregated value.
   */
  Object getAggregate();
}
