package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Basic information about the server API.
 * 
 * @author junglas
 */
@XmlType(name = "ping-result")
@XmlRootElement(name = "ping-result")
public class PingResult
{
  /** The release of the server. */
  private String m_serverVersion;
  /** Major version number of the API. */
  private int m_apiVersionMajor;
  /** Minor version number of the API. */
  private int m_apiVersionMinor;

  public String getServerVersion()
  {
    return m_serverVersion;
  }

  public void setServerVersion(final String serverVersion)
  {
    m_serverVersion = serverVersion;
  }

  public int getApiVersionMajor()
  {
    return m_apiVersionMajor;
  }

  public void setApiVersionMajor(final int apiVersionMajor)
  {
    m_apiVersionMajor = apiVersionMajor;
  }

  public int getApiVersionMinor()
  {
    return m_apiVersionMinor;
  }

  public void setApiVersionMinor(final int apiVersionMinor)
  {
    m_apiVersionMinor = apiVersionMinor;
  }

}
