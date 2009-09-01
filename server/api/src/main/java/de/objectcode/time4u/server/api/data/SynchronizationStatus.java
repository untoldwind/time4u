package de.objectcode.time4u.server.api.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The synchronization status of a certain entity type.
 * 
 * @author junglas
 */
@XmlType(name = "synchronization-status")
@XmlRootElement(name = "synchronization-status")
public class SynchronizationStatus implements Serializable
{
  private static final long serialVersionUID = 7066442424410214823L;

  private EntityType m_entityType;
  private long m_lastSendRevision;
  private long m_lastReceivedRevision;

  public EntityType getEntityType()
  {
    return m_entityType;
  }

  public void setEntityType(final EntityType entityType)
  {
    m_entityType = entityType;
  }

  public long getLastSendRevision()
  {
    return m_lastSendRevision;
  }

  public void setLastSendRevision(final long lastSendRevision)
  {
    m_lastSendRevision = lastSendRevision;
  }

  public long getLastReceivedRevision()
  {
    return m_lastReceivedRevision;
  }

  public void setLastReceivedRevision(final long lastReceivedRevision)
  {
    m_lastReceivedRevision = lastReceivedRevision;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("SynchronizationStatus(");
    buffer.append("entityType=").append(m_entityType);
    buffer.append(", lastSendRevision=").append(m_lastSendRevision);
    buffer.append(", lastReceivedRevision=").append(m_lastReceivedRevision);
    buffer.append(")");

    return buffer.toString();
  }

}
