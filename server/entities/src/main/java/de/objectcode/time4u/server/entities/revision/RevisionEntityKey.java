package de.objectcode.time4u.server.entities.revision;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public class RevisionEntityKey implements Serializable
{
  private static final long serialVersionUID = -143072929625119307L;

  private EntityType m_entityKey;
  private long m_clientPart;
  private long m_localPart;

  protected RevisionEntityKey()
  {
  }

  public RevisionEntityKey(final EntityType entityKey, final UUID uuid)
  {
    m_clientPart = uuid != null ? uuid.getMostSignificantBits() : -1L;
    m_localPart = uuid != null ? uuid.getLeastSignificantBits() : -1L;
    m_entityKey = entityKey;
  }

  public RevisionEntityKey(final EntityType entityKey, final long clientPart, final long localPart)
  {
    m_clientPart = clientPart;
    m_entityKey = entityKey;
    m_localPart = localPart;
  }

  public int getEntityKeyValue()
  {
    return m_entityKey != null ? m_entityKey.getValue() : -1;
  }

  public void setEntityKeyValue(final int value)
  {
    m_entityKey = EntityType.forValue(value);
  }

  public long getClientPart()
  {
    return m_clientPart;
  }

  public void setClientPart(final long clientPart)
  {
    m_clientPart = clientPart;
  }

  public long getLocalPart()
  {
    return m_localPart;
  }

  public void setLocalPart(final long localPart)
  {
    m_localPart = localPart;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == null || !(obj instanceof RevisionEntityKey)) {
      return false;
    }

    final RevisionEntityKey castObj = (RevisionEntityKey) obj;

    return m_entityKey == castObj.m_entityKey && m_clientPart == castObj.m_clientPart
        && m_localPart == castObj.m_localPart;
  }

  @Override
  public int hashCode()
  {
    int hash = m_entityKey != null ? m_entityKey.getValue() : -1;

    hash = hash + 13 * (int) (m_clientPart ^ m_clientPart >>> 32 ^ m_localPart ^ m_localPart >>> 32);

    return hash;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("RevisionEntityKey(");
    buffer.append("entityKey=").append(m_entityKey);
    buffer.append(", clientPart=").append(m_clientPart);
    buffer.append(", localPart=").append(m_localPart);
    buffer.append(")");

    return buffer.toString();
  }

}
