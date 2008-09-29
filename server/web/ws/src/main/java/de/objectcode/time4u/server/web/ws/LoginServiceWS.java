package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;

import de.objectcode.time4u.server.api.ILoginService;

@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.ILoginService")
public class LoginServiceWS implements ILoginService
{

  public boolean checkLogin(final String userId)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean registerLogin(final String userId, final String password, final String name, final String email)
  {
    // TODO Auto-generated method stub
    return false;
  }

}
