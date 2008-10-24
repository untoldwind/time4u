package de.objectcode.time4u.server.web.ws;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WSContextListener implements ServletContextListener
{
  private final static Log LOG = LogFactory.getLog(WSContextListener.class);

  JmDNS m_jmdns;

  public void contextInitialized(final ServletContextEvent event)
  {
    LOG.info("Initializing...");

    try {
      m_jmdns = new JmDNS();

      final ServiceInfo serviceInfo = new ServiceInfo("_time4u._http._tcp.local.", "Time4UWS", 8080,
          "Time4U WebService");

      m_jmdns.registerService(serviceInfo);

    } catch (final IOException e) {
      LOG.error("Exception", e);
    }
    LOG.info("Initialized");
  }

  public void contextDestroyed(final ServletContextEvent event)
  {
    LOG.info("Destroying...");

    if (m_jmdns != null) {
      m_jmdns.unregisterAllServices();
      m_jmdns.close();
      m_jmdns = null;
    }
  }

}
