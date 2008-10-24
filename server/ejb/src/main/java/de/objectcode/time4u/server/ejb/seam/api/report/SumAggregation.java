package de.objectcode.time4u.server.ejb.seam.api.report;

public class SumAggregation implements IAggregation
{
  int m_sum = 0;

  public void collect(final Object value)
  {
    m_sum += (Integer) value;
  }

  public void finish()
  {
  }

  public Object getAggregate()
  {
    return m_sum;
  }

}
