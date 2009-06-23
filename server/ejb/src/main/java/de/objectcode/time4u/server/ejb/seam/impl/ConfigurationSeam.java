package de.objectcode.time4u.server.ejb.seam.impl;

import static de.objectcode.time4u.server.ejb.config.IConfigurationKeys.CONTEXT_SERVER;
import static de.objectcode.time4u.server.ejb.config.IConfigurationKeys.LOGIN_AUTOREGISTRATION_ENABLED;
import static de.objectcode.time4u.server.ejb.config.IConfigurationKeys.SERVER_URL;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

import de.objectcode.time4u.server.ejb.config.IConfigurationServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.ConfigurationData;
import de.objectcode.time4u.server.ejb.seam.api.IConfigurationLocal;

@Stateless
@Local(IConfigurationLocal.class)
@org.jboss.annotation.ejb.LocalBinding(jndiBinding = "time4u-server/seam/ConfigurationSeam/local")
@org.jboss.ejb3.annotation.LocalBinding(jndiBinding = "time4u-server/seam/ConfigurationSeam/local")
@Name("Configuration")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class ConfigurationSeam implements IConfigurationLocal
{
  @EJB
  IConfigurationServiceLocal m_configurationService;

  public ConfigurationData getConfiguration()
  {
    final ConfigurationData configuration = new ConfigurationData();

    configuration.setAutoRegistrationEnabled(m_configurationService.getBooleanValue(CONTEXT_SERVER,
        LOGIN_AUTOREGISTRATION_ENABLED, true));
    configuration.setServerUrl(m_configurationService.getStringValue(CONTEXT_SERVER, SERVER_URL, "http://localhost"));

    return configuration;
  }

  @Restrict("#{s:hasRole('admin')}")
  public void setConfiguration(final ConfigurationData configuration)
  {
    m_configurationService.setBooleanValue(CONTEXT_SERVER, LOGIN_AUTOREGISTRATION_ENABLED, configuration
        .isAutoRegistrationEnabled());
    m_configurationService.setStringValue(CONTEXT_SERVER, SERVER_URL, configuration.getServerUrl());
  }
}
