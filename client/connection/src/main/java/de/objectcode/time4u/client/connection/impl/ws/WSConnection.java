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
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.IConstants;
import de.objectcode.time4u.server.api.ILoginService;
import de.objectcode.time4u.server.api.IPingService;
import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PingResult;
import de.objectcode.time4u.server.api.data.RegistrationInfo;
import de.objectcode.time4u.server.api.data.RevisionStatus;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

public class WSConnection implements IConnection
{
  private final IPingService m_pingService;
  private final ILoginService m_loginService;
  private final IRevisionService m_revisionService;

  public WSConnection(final URL url, final Map<String, String> credentials) throws ConnectionException
  {
    m_pingService = getServicePort(url, "PingService", IPingService.class, credentials, false);
    m_loginService = getServicePort(url, "LoginService", ILoginService.class, credentials, false);
    m_revisionService = getServicePort(url, "RevisionService", IRevisionService.class, credentials, true);
  }

  public boolean testConnection() throws ConnectionException
  {
    try {
      final PingResult pingResult = m_pingService.ping();

      return pingResult.getApiVersionMajor() == IConstants.API_VERSION_MAJOR;
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  public boolean checkLogin(final Map<String, String> credentials) throws ConnectionException
  {
    try {
      return m_loginService.checkLogin(credentials.get("userId"));
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  public RevisionStatus getRevisionStatus() throws ConnectionException
  {
    try {
      return m_revisionService.getRevisionStatus();
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  public boolean registerLogin(final Map<String, String> credentials) throws ConnectionException
  {
    try {
      final IPasswordEncoder encoder = new DefaultPasswordEncoder();

      final Person owner = RepositoryFactory.getRepository().getOwner();
      final RegistrationInfo registrationInfo = new RegistrationInfo();

      registrationInfo.setClientId(RepositoryFactory.getRepository().getClientId());
      registrationInfo.setPersonId(owner.getId());
      registrationInfo.setName(owner.getName());
      registrationInfo.setEmail(owner.getEmail());
      registrationInfo.setUserId(credentials.get("userId"));
      registrationInfo.setHashedPassword(encoder.encrypt(credentials.get("password").toCharArray()));

      return m_loginService.registerLogin(registrationInfo);
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  private <T> T getServicePort(final URL baseUrl, final String serviceName, final Class<T> portInterface,
      final Map<String, String> credentials, final boolean secure) throws ConnectionException
  {
    final URL wsdl = getClass().getResource(serviceName + ".wsdl");
    final Service service = Service.create(wsdl, new QName("http://objectcode.de/time4u/api/ws", serviceName
        + "WSService"));

    final T port = service.getPort(portInterface);

    try {
      final BindingProvider bp = (BindingProvider) port;
      bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
          new URL(baseUrl, "/time4u-ws" + (secure ? "/secure/" : "/") + serviceName).toString());
      if (secure) {
        bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, credentials.get("userId"));
        bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, credentials.get("password"));
      }
    } catch (final MalformedURLException e) {
      throw new ConnectionException("Malformed url", e);
    }

    return port;
  }

  public static void main(final String[] args)
  {
    try {
      final Map<String, String> cred = new HashMap<String, String>();
      cred.put("userId", "admin");
      cred.put("password", "admin");
      final WSConnection con = new WSConnection(new URL("http://localhost:8080"), cred);

      System.out.println(">>" + con.testConnection());

      System.out.println(">" + con.getRevisionStatus());
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
