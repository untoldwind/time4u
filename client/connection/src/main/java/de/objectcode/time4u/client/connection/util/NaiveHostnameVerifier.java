package de.objectcode.time4u.client.connection.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NaiveHostnameVerifier implements HostnameVerifier
{

  public boolean verify(final String hostname, final SSLSession session)
  {
    return true;
  }

}
