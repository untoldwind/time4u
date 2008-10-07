package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.Person;

/**
 * Remote person service interface.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
public interface IPersonService
{
  /**
   * Get information about myself (i.e. the person who is logged in).
   * 
   * @return Personal information of the current user.
   */
  @WebMethod
  Person getSelf();

  /**
   * Register a new client for this user.
   * 
   * Note: It is okay if there is already a client with the same id for the current user (reregister), but it is not
   * okay if there is already the same client id for a different person.
   */
  @WebMethod
  boolean registerClient(long clientId);
}
