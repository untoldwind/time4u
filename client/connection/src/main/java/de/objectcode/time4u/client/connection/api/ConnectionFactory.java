package de.objectcode.time4u.client.connection.api;

import java.net.MalformedURLException;
import java.net.URL;

import de.objectcode.time4u.client.connection.impl.ws.WSConnection;
import de.objectcode.time4u.server.api.data.ServerConnection;

public class ConnectionFactory
{
  public static IConnection openConnection(final ServerConnection serverConnection) throws ConnectionException
  {
    if (serverConnection.getUrl() == null) {
      return null;
    }

    try {
      final URL url = new URL(serverConnection.getUrl());

      if ("http".equals(url.getProtocol()) || "https".equals(url.getProtocol())) {
        return new WSConnection(url, serverConnection.getCredentials());
      }
    } catch (final MalformedURLException e) {
      throw new ConnectionException("Malformed url", e);
    }

    return null;
  }
}
