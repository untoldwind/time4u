package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.ILoginService;
import de.objectcode.time4u.server.api.data.RegistrationInfo;

/**
 * Web-service delegate to the login service implementation.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.ILoginService")
public class LoginServiceWS implements ILoginService
{
  private static final Log LOG = LogFactory.getLog(LoginServiceWS.class);

  private final ILoginService m_loginService;

  public LoginServiceWS() throws Exception
  {
    final InitialContext ctx = new InitialContext();

    m_loginService = (ILoginService) ctx.lookup("time4u-server/LoginService/remote");
  }

  public boolean checkLogin(final String userId)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("checkLogin: " + userId);
    }
    return m_loginService.checkLogin(userId);
  }

  public boolean registerLogin(final RegistrationInfo registrationInfo)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("registerLogin: " + registrationInfo);
    }
    return m_loginService.registerLogin(registrationInfo);
  }

}
