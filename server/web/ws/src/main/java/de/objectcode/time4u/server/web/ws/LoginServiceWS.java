package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.ILoginService;

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

  public boolean registerLogin(final String userId, final String hashedPassword, final String name, final String email)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("registerLogin: " + userId + " " + name + " " + email);
    }
    return m_loginService.registerLogin(userId, hashedPassword, name, email);
  }

}
