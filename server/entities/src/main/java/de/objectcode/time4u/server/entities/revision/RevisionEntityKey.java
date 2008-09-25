package de.objectcode.time4u.server.entities.revision;

import java.io.Serializable;

public class RevisionEntityKey implements Serializable
{
  private static final long serialVersionUID = -143072929625119307L;

  private EntityType m_entityKey;
  private long m_part;

  protected RevisionEntityKey()
  {
  }

  public RevisionEntityKey(final EntityType entityKey, final long part)
  {
    m_entityKey = entityKey;
    m_part = part;
  }

  public int getEntityKeyValue()
  {
    return m_entityKey != null ? m_entityKey.getValue() : -1;
  }

  public void setEntityKeyValue(final int value)
  {
    m_entityKey = EntityType.forValue(value);
  }

  public long getPart()
  {
    return m_part;
  }

  public void setPart(final long part)
  {
    m_part = part;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == null || !(obj instanceof RevisionEntityKey)) {
      return false;
    }

    final RevisionEntityKey castObj = (RevisionEntityKey) obj;

    return m_entityKey == castObj.m_entityKey && m_part == castObj.m_part;
  }

  @Override
  public int hashCode()
  {
    int hash = m_entityKey != null ? m_entityKey.getValue() : -1;

    hash = hash + 13 * (int) (m_part ^ m_part >>> 32);

    return hash;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("RevisionEntityKey(");
    buffer.append("entityKey=").append(m_entityKey);
    buffer.append(", part=").append(m_part);
    buffer.append(")");

    return buffer.toString();
  }

}
