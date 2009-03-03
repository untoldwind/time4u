package de.objectcode.time4u.server.ejb.seam.api;

import java.io.Serializable;

public class ConfigurationData implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -9175728017978463204L;
  boolean autoRegistrationEnabled;

  public boolean isAutoRegistrationEnabled()
  {
    return autoRegistrationEnabled;
  }

  public void setAutoRegistrationEnabled(final boolean autoRegistrationEnabled)
  {
    this.autoRegistrationEnabled = autoRegistrationEnabled;
  }

}
