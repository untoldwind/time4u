package de.objectcode.time4u.server.ejb.seam.api;

public interface IConfigurationLocal
{
  ConfigurationData getConfiguration();

  void setConfiguration(ConfigurationData configuration);
}
