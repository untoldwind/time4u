package de.objectcode.time4u.client.connection.api;

import java.util.Map;

public interface IConnection
{
  boolean testConnection() throws ConnectionException;

  boolean checkLogin(Map<String, String> credentials) throws ConnectionException;

  boolean registerLogin(Map<String, String> credentials) throws ConnectionException;
}
