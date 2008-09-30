package de.objectcode.time4u.server.api.data;

import java.util.Date;

/**
 * Person DTO object.
 * 
 * @author junglas
 */
public class Person implements ISynchronizableData
{
  private static final long serialVersionUID = -4022407515199667835L;

  /** Internal server id of the person. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** User id of the person. */
  private String m_userId;
  /** Real name of the person. */
  private String m_name;
  /** Email address of the person. */
  private String m_email;
  /** Timestamp of the last synchronization of that person. */
  private Date m_lastSynchronize;

  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
  {
    m_id = id;
  }

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  public String getName()
  {
    return m_name;
  }

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  public String getUserId()
  {
    return m_userId;
  }

  public void setUserId(final String userId)
  {
    m_userId = userId;
  }

  public String getEmail()
  {
    return m_email;
  }

  public void setEmail(final String email)
  {
    m_email = email;
  }

  public Date getLastSynchronize()
  {
    return m_lastSynchronize;
  }

  public void setLastSynchronize(final Date lastSynchronize)
  {
    m_lastSynchronize = lastSynchronize;
  }
}
