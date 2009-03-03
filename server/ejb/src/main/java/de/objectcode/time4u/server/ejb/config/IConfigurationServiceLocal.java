package de.objectcode.time4u.server.ejb.config;

public interface IConfigurationServiceLocal
{
  boolean getBooleanValue(String contextId, String name, boolean defaulValue);

  void setBooleanValue(String contextId, String name, boolean value);

  long getLongValue(String contextId, String name, long defaulValue);

  void setLongValue(String contextId, String name, long value);

  String getStringValue(String contextId, String name, String defaulValue);

  void setStringValue(String contextId, String name, String value);
}
