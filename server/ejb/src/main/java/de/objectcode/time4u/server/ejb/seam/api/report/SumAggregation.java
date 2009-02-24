package de.objectcode.time4u.server.ejb.seam.api.report;

/**
 * SumAggrated simple sums up an integer based value.
 * 
 * @author junglas
 */
public class SumAggregation implements IAggregation
{
  int m_sum = 0;

  /**
   * {@inheritDoc}
   */
  public void collect(final Object value)
  {
    m_sum += (Integer) value;
  }

  /**
   * {@inheritDoc}
   */
  public void finish()
  {
  }

  /**
   * {@inheritDoc}
   */
  public Object getAggregate()
  {
    return m_sum;
  }

}
