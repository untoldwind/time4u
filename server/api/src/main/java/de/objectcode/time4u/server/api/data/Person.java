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
  private long m_id;
  /** User id of the person. */
  private String m_userId;
  /** Real name of the person. */
  private String m_name;
  /** Email address of the person. */
  private String m_email;
  /** Timestamp of the last synchronization of that person. */
  private Date m_lastSynchronize;

  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  public String getName()
  {
    return m_name;
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
