package de.objectcode.time4u.server.api.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "synchronization-status")
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

}
