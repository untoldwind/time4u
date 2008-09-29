package de.objectcode.time4u.server.api;

import de.objectcode.time4u.server.api.data.PingResult;

/**
 * A simple ping service.
 * 
 * The client should use this service to do a "are you there" check and a "are you compatible with me" check.
 * 
 * @author junglas
 */
public interface IPingService
{
  /**
   * Ping the server.
   * 
   * @return Some basic information about the server.
   * @throws ServiceException
   *           on error
   */
  PingResult ping() throws ServiceException;
}
