package de.objectcode.time4u.client.connection.impl.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.api.IConnection;
import de.objectcode.time4u.server.api.IPingService;

public class WSConnection implements IConnection
{
  IPingService m_pingService;

  public WSConnection(final URL url, final Map<String, String> credentials)
  {
    final URL pingServiceWSDL = getClass().getResource("ping-service.wsdl");
    final Service service = Service.create(pingServiceWSDL, new QName("http://objectcode.de/time4u/api/ws",
        "PingServiceWSService"));
    m_pingService = service.getPort(IPingService.class);
    final BindingProvider bp = (BindingProvider) m_pingService;
    try {
      bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
          new URL(url, "/time4u-ws/PingService").toString());
    } catch (final MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println(">> " + m_pingService);

    System.out.println(">> " + m_pingService.ping().getApiVersionMajor());
  }

  public boolean testConnection() throws ConnectionException
  {
    // TODO Auto-generated method stub
    return false;
  }

  public static void main(final String[] args)
  {
    try {
      final Map<String, String> cred = new HashMap<String, String>();

      final WSConnection con = new WSConnection(new URL("http://localhost:8080"), cred);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
