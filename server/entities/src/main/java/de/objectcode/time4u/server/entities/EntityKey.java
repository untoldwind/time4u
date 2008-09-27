package de.objectcode.time4u.server.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class EntityKey implements Serializable
{
  private static final long serialVersionUID = 696429215327536449L;

  private long m_clientId;
  private long m_localId;

  protected EntityKey()
  {
  }

  public EntityKey(final long clientId, final long localId)
  {
    m_clientId = clientId;
    m_localId = localId;
  }

  public EntityKey(final UUID uuid)
  {
    m_clientId = uuid.getMostSignificantBits();
    m_localId = uuid.getLeastSignificantBits();
  }

  public long getClientId()
  {
    return m_clientId;
  }

  public void setClientId(final long clientId)
  {
    m_clientId = clientId;
  }

  public long getLocalId()
  {
    return m_localId;
  }

  public void setLocalId(final long localId)
  {
    m_localId = localId;
  }

  @Transient
  public UUID getUUID()
  {
    return new UUID(m_clientId, m_localId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof EntityKey)) {
      return false;
    }

    final EntityKey castObj = (EntityKey) obj;

    return m_clientId == castObj.m_clientId && m_localId == castObj.m_localId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return (int) (m_clientId >> 32 ^ m_clientId ^ m_localId >> 32 ^ m_localId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("EntityKey(");
    buffer.append("clientId=").append(m_clientId);
    buffer.append(",localId=").append(m_localId);
    buffer.append(")");

    return buffer.toString();
  }

}
