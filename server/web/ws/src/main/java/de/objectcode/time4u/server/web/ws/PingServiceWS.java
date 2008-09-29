package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;

import de.objectcode.time4u.server.api.IConstants;
import de.objectcode.time4u.server.api.IPingService;
import de.objectcode.time4u.server.api.data.PingResult;

@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.IPingService")
public class PingServiceWS implements IPingService
{
  public PingResult ping()
  {
    final PingResult result = new PingResult();

    // Report server version of the API package
    result.setApiVersionMajor(IConstants.API_VERSION_MAJOR);
    result.setApiVersionMinor(IConstants.API_VERSION_MINOR);

    return result;
  }
}
