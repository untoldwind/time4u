package de.objectcode.time4u.client.store.backend;

import java.io.File;

public interface IDatabaseBackend
{
  public String getHibernateDialect();

  public String getJdbcDriver();

  public String getJdbcUrl(final File directory);

  public String getJdbcUserName();

  public String getJdbcPassword();

}
