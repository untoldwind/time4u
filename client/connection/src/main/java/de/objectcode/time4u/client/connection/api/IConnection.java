package de.objectcode.time4u.client.connection.api;

import java.util.Map;

/**
 * Client side connection API.
 * 
 * @author junglas
 */
public interface IConnection
{
  boolean testConnection() throws ConnectionException;

  boolean checkLogin(Map<String, String> credentials) throws ConnectionException;

  boolean registerLogin(Map<String, String> credentials) throws ConnectionException;

  void sychronizeNow() throws ConnectionException;
}
