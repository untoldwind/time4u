package de.objectcode.time4u.client.connection.api;

import java.net.MalformedURLException;
import java.net.URL;

import de.objectcode.time4u.client.connection.impl.ws.WSConnection;
import de.objectcode.time4u.server.api.data.ServerConnection;

/**
 * Main factory for the client side connection API.
 * 
 * All other plugins should use this factory to obtain an implementation of the connection interfaces.
 * 
 * @author junglas
 */
public class ConnectionFactory
{
  /**
   * Open a server connection.
   * 
   * @param serverConnection
   *          The server connection to open.
   * @return The server connection
   * @throws ConnectionException
   *           on error
   */
  public static IConnection openConnection(final ServerConnection serverConnection) throws ConnectionException
  {
    if (serverConnection.getUrl() == null) {
      return null;
    }

    try {
      final URL url = new URL(serverConnection.getUrl());

      if ("http".equals(url.getProtocol()) || "https".equals(url.getProtocol())) {
        return new WSConnection(serverConnection);
      }
    } catch (final MalformedURLException e) {
      throw new ConnectionException("Malformed url", e);
    }

    return null;
  }
}
