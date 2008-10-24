package de.objectcode.time4u.server.entities;

import java.io.Serializable;

public class MetaPropertyKey implements Serializable
{
  private static final long serialVersionUID = 6165334537280958145L;

  String m_entityId;
  String m_name;

  /**
   * Default constructor for hibernate.
   */
  protected MetaPropertyKey()
  {
  }

  public MetaPropertyKey(final String entityId, final String name)
  {
    m_entityId = entityId;
    m_name = name;
  }

  public String getEntityId()
  {
    return m_entityId;
  }

  public void setEntityId(final String entityId)
  {
    m_entityId = entityId;
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
    if (!(obj instanceof MetaPropertyKey)) {
      return false;
    }

    final MetaPropertyKey castObj = (MetaPropertyKey) obj;

    return m_entityId.equals(castObj.m_entityId) && m_name.equals(castObj.m_name);
  }

  @Override
  public int hashCode()
  {
    int hash = m_entityId.hashCode();

    hash = 13 * hash + m_name.hashCode();

    return hash;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("MetaPropertyKey(");
    buffer.append("entityId=").append(m_entityId);
    buffer.append(", name=").append(m_name);
    buffer.append(")");

    return buffer.toString();
  }

}
