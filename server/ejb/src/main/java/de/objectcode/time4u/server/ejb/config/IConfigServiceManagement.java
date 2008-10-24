package de.objectcode.time4u.server.ejb.config;

public interface IConfigServiceManagement
{
  long getServerId();

  void start() throws Exception;

  void stop();
}
