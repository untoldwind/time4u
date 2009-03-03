package de.objectcode.time4u.server.entities.config;

import java.io.Serializable;

public class ConfigurationKey implements Serializable
{
  private static final long serialVersionUID = -2232811915457070249L;

  String m_contextId;
  String m_name;

  /**
   * Default constructor for hibernate.
   */
  protected ConfigurationKey()
  {
  }

  public ConfigurationKey(final String contextId, final String name)
  {
    m_contextId = contextId;
    m_name = name;
  }

  public String getContextId()
  {
    return m_contextId;
  }

  public void setContextId(final String contextId)
  {
    m_contextId = contextId;
  }

  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ConfigurationKey)) {
      return false;
    }

    final ConfigurationKey castObj = (ConfigurationKey) obj;

    return m_contextId.equals(castObj.m_contextId) && m_name.equals(castObj.m_name);
  }

  @Override
  public int hashCode()
  {
    int hash = m_contextId.hashCode();

    hash = 13 * hash + m_name.hashCode();

    return hash;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("ConfigurationKey(");
    buffer.append("contextId=").append(m_contextId);
    buffer.append(", name=").append(m_name);
    buffer.append(")");

    return buffer.toString();
  }

}
