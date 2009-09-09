package de.objectcode.time4u.client.store.derby;

import java.io.File;

import de.objectcode.time4u.client.store.backend.IDatabaseBackend;

/**
 * Derby database backend.
 * 
 * @author junglas
 */
public class DerbyBackend implements IDatabaseBackend
{
  /**
   * {@inheritDoc}
   */
  public String getHibernateDialect()
  {
    return "org.hibernate.dialect.DerbyDialect";
  }

  /**
   * {@inheritDoc}
   */
  public String getJdbcDriver()
  {
    return "org.apache.derby.jdbc.EmbeddedDriver";
  }

  /**
   * {@inheritDoc}
   */
  public String getJdbcUrl(final File directory)
  {
    System.setProperty("derby.system.home", directory.getAbsolutePath());

    return "jdbc:derby:" + directory.getAbsolutePath() + "/time4u;create=true";
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
