package de.objectcode.time4u.server.shout;

import java.io.Serializable;
import java.net.URL;

public class ShoutData implements Serializable
{
  private static final long serialVersionUID = 3702206238584406265L;

  private final URL m_webserviceBaseURL;

  public ShoutData(final URL webserviceBaseURL)
  {
    m_webserviceBaseURL = webserviceBaseURL;
  }

  public URL getWebserviceBaseURL()
  {
    return m_webserviceBaseURL;
  }
}
