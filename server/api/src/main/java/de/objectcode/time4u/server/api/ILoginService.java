package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
public interface ILoginService
{
  @WebMethod
  boolean checkLogin(String userId);

  @WebMethod
  boolean registerLogin(String userId, String hashedPassword, String name, String email);
}
