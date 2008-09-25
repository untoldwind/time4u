package de.objectcode.time4u.server.entities.revision;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "T4U_REVISIONS")
@IdClass(RevisionEntityKey.class)
public class RevisionEntity
{
  private EntityType m_entityKey;
  private long m_part;
  private long m_latestRevision;

  protected RevisionEntity()
  {
  }

  public RevisionEntity(final EntityType entityKey, final long part)
  {
    m_entityKey = entityKey;
    m_part = part;
    m_latestRevision = 0L;
  }

  @Id
  public int getEntityKeyValue()
  {
    return m_entityKey != null ? m_entityKey.getValue() : -1;
  }

  public void setEntityKeyValue(final int value)
  {
    m_entityKey = EntityType.forValue(value);
  }

  @Id
  public long getPart()
  {
    return m_part;
  }

  public void setPart(final long part)
  {
    m_part = part;
  }

  public long getLatestRevision()
  {
    return m_latestRevision;
  }

  public void setLatestRevision(final long latestRevision)
  {
    m_latestRevision = latestRevision;
  }

}
