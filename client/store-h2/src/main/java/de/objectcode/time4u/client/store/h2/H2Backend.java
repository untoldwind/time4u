package de.objectcode.time4u.client.store.h2;

import java.io.File;

import de.objectcode.time4u.client.store.backend.IDatabaseBackend;

/**
 * Derby database backend.
 * 
 * @author junglas
 */
public class H2Backend implements IDatabaseBackend
{
  /**
   * {@inheritDoc}
   */
  public String getHibernateDialect()
  {
    return "org.hibernate.dialect.H2Dialect";
  }

  /**
   * {@inheritDoc}
   */
  public String getJdbcDriver()
  {
    return "org.h2.Driver";
  }

  /**
   * {@inheritDoc}
   */
  public String getJdbcUrl(final File directory)
  {
    return "jdbc:h2:" + directory.getAbsolutePath() + "/time4u";
  }

  /**
   * {@inheritDoc}
   */
  public String getJdbcUserName()
  {
    return "sa";
  }

  /**
   * {@inheritDoc}
   */
  public String getJdbcPassword()
  {
    return "";
  }
}
