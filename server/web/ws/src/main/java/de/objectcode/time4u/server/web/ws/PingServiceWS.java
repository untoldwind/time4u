package de.objectcode.time4u.server.web.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.IConstants;
import de.objectcode.time4u.server.api.IPingService;
import de.objectcode.time4u.server.api.ServiceException;
import de.objectcode.time4u.server.api.data.PingResult;

@WebService
@SOAPBinding(style = Style.RPC)
public class PingServiceWS implements IPingService
{
  @WebMethod
  public PingResult ping() throws ServiceException
  {
    final PingResult result = new PingResult();

    // Report server version of the API package
    result.setApiVersionMajor(IConstants.API_VERSION_MAJOR);
    result.setApiVersionMinor(IConstants.API_VERSION_MINOR);

    return result;
  }
}
