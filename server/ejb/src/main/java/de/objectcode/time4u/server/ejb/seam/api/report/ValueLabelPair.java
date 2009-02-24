package de.objectcode.time4u.server.ejb.seam.api.report;

/**
 * Helper class to transfer a generic value with label.
 * 
 * @author junglas
 */
public class ValueLabelPair
{
  Object m_value;
  Object m_label;

  public ValueLabelPair(final Object value, final Object label)
  {
    m_value = value;
    m_label = label;
  }

  public Object getValue()
  {
    return m_value;
  }

  public Object getLabel()
  {
    return m_label;
  }

}
