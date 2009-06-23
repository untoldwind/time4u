package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.ConfigurationData;
import de.objectcode.time4u.server.ejb.seam.api.IConfigurationLocal;

@Name("admin.configurationController")
@Scope(ScopeType.CONVERSATION)
public class ConfigurationController
{
  public static final String VIEW_ID = "/admin/config.xhtml";

  @In("Configuration")
  IConfigurationLocal m_configuration;

  @In("user.configuration")
  ConfigurationData m_configurationData;

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String updateConfiguration()
  {
    m_configuration.setConfiguration(m_configurationData);

    return VIEW_ID;
  }
}
