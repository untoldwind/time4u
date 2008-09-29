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
    m_nextLocalId = (long) id.getEntityKeyValue() << 56 | 1L;
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

  public String generateId(final long clientId)
  {
    final long localId = m_nextLocalId++;
    final StringBuffer buffer = new StringBuffer();

    buffer.append(digits(clientId >> 32, 8));
    buffer.append(digits(clientId, 8));
    buffer.append('-');
    buffer.append(digits(localId >> 56, 2));
    buffer.append('-');
    buffer.append(digits(localId, 14));

    return buffer.toString();
  }

  private static String digits(final long val, final int digits)
  {
    final long hi = 1L << digits * 4;
    return Long.toHexString(hi | val & hi - 1).substring(1);
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
