package de.objectcode.time4u.server.api.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Server connection DTO.
 * 
 * Contains all relevant information of a connection to a remote server.
 * 
 * @author junglas
 */
public class ServerConnection implements Serializable
{
  private static final long serialVersionUID = 524152758167415304L;

  /** Primary key. */
  private long m_id;
  /** Root project to be synchronized. */
  private String m_rootProjectId;
  /** Logical name of the server. */
  private String m_name;
  /** Connection url. */
  private String m_url;
  /** Server credentials. */
  private Map<String, String> m_credentials;
  /** Timestamp of the last synchronization. */
  private Date m_lastSynchronize;
  /** Synchronize every x seconds (0 = never). */
  private int m_synchronizeInterval;
  /** It might be necessary to map the local person id to a different person id on the server. */
  private String m_mappedPersonId;

  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  public String getRootProjectId()
  {
    return m_rootProjectId;
  }

  public void setRootProjectId(final String rootProjectId)
  {
    m_rootProjectId = rootProjectId;
  }

  public String getUrl()
  {
    return m_url;
  }

  public void setUrl(final String url)
  {
    m_url = url;
  }

  public Map<String, String> getCredentials()
  {
    return m_credentials;
  }

  public void setCredentials(final Map<String, String> credentials)
  {
    m_credentials = credentials;
  }

  public Date getLastSynchronize()
  {
    return m_lastSynchronize;
  }

  public void setLastSynchronize(final Date lastSynchronize)
  {
    m_lastSynchronize = lastSynchronize;
  }

  public int getSynchronizeInterval()
  {
    return m_synchronizeInterval;
  }

  public void setSynchronizeInterval(final int synchronizeInterval)
  {
    m_synchronizeInterval = synchronizeInterval;
  }

  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  public String getMappedPersonId()
  {
    return m_mappedPersonId;
  }

  public void setMappedPersonId(final String mappedPersonId)
  {
    m_mappedPersonId = mappedPersonId;
  }
}
