package de.objectcode.time4u.server.api.data;

import java.io.Serializable;
import java.util.Map;

public class ServerConnection implements Serializable
{
  private static final long serialVersionUID = 524152758167415304L;

  /** Primary key */
  private long m_id;
  /** Root project to be synchronized */
  private String m_rootProjectId;
  /** Connection url */
  private String m_url;
  /** Server credentials */
  private Map<String, String> m_credentials;

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

}
