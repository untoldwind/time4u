package de.objectcode.time4u.server.entities.revision;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T4U_REVISIONS")
public class RevisionEntity implements IRevisionLock
{
  private RevisionEntityKey m_id;
  private long m_latestRevision;
  private long m_nextLocalId;

  protected RevisionEntity()
  {
  }

  public RevisionEntity(final RevisionEntityKey id)
  {
    m_id = id;
    m_latestRevision = 0L;
    m_nextLocalId = 1L;
  }

  @Id
  public RevisionEntityKey getId()
  {
    return m_id;
  }

  public void setId(final RevisionEntityKey id)
  {
    m_id = id;
  }

  public long getLatestRevision()
  {
    return m_latestRevision;
  }

  public void setLatestRevision(final long latestRevision)
  {
    m_latestRevision = latestRevision;
  }

  public long generateLocalId()
  {
    return m_nextLocalId++;
  }

  public long getNextLocalId()
  {
    return m_nextLocalId;
  }

  public void setNextLocalId(final long nextLocalId)
  {
    m_nextLocalId = nextLocalId;
  }

}
