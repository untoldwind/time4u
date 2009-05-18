package de.objectcode.time4u.client.connection.util;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NaiveTrustManager implements X509TrustManager
{

  public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException
  {
  }

  public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException
  {
  }

  public X509Certificate[] getAcceptedIssuers()
  {
    return null;
  }

  public static SSLSocketFactory getSSLSocketFactory()
  {
    try {
      final TrustManager[] tm = new TrustManager[] { new NaiveTrustManager() };
      final SSLContext context = SSLContext.getInstance("SSL");
      context.init(new KeyManager[0], tm, new SecureRandom());

      return context.getSocketFactory();
    } catch (final Exception e) {
      e.printStackTrace();
      throw new RuntimeException("SSL error: ", e);
    }
  }
}
