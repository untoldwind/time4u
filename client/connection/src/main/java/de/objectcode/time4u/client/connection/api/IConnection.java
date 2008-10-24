package de.objectcode.time4u.client.connection.api;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import de.objectcode.time4u.server.api.data.Person;

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

  Person getPerson() throws ConnectionException;

  boolean registerClient() throws ConnectionException;

  void sychronizeNow(IProgressMonitor monitor) throws ConnectionException;
}
