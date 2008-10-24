package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.PingResult;

/**
 * A simple ping service.
 * 
 * The client should use this service to do a "are you there" check and a "are you compatible with me" check.
 * 
 * @author junglas
 */
@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
public interface IPingService
{
  /**
   * Ping the server.
   * 
   * @return Some basic information about the server.
   * @throws ServiceException
   *           on error
   */
  @WebMethod
  PingResult ping();
}
