package de.objectcode.time4u.server.ejb.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;

import de.objectcode.time4u.server.entities.config.ConfigValueType;
import de.objectcode.time4u.server.entities.config.ConfigurationEntity;

@Stateless
@Local(IConfigurationServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/ConfigurationService/local")
public class ConfigurationServiceImpl implements IConfigurationServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  Map<String, Object> m_configuration;

  public boolean getBooleanValue(final String contextId, final String name, final boolean defaulValue)
  {
    if (m_configuration == null) {
      initializeConfiguration();
    }
    final Object value = m_configuration.get(contextId + ":" + name);

    if (value == null || !(value instanceof Boolean)) {
      return (Boolean) value;
    }

    return defaulValue;
  }

  public long getLongValue(final String contextId, final String name, final long defaulValue)
  {
    if (m_configuration == null) {
      initializeConfiguration();
    }

    final Object value = m_configuration.get(contextId + ":" + name);

    if (value == null || !(value instanceof Long)) {
      return (Long) value;
    }

    return defaulValue;
  }

  public String getStringValue(final String contextId, final String name, final String defaulValue)
  {
    if (m_configuration == null) {
      initializeConfiguration();
    }

    final Object value = m_configuration.get(contextId + ":" + name);

    if (value == null || !(value instanceof String)) {
      return (String) value;
    }

    return defaulValue;
  }

  public void setBooleanValue(final String contextId, final String name, final boolean value)
  {
    if (m_configuration == null) {
      initializeConfiguration();
    }

    final ConfigurationEntity entity = new ConfigurationEntity(contextId, name, ConfigValueType.BOOLEAN);

    entity.setBooleanValue(value);

    m_manager.merge(entity);

    m_configuration.put(contextId + ":" + name, value);
  }

  public void setLongValue(final String contextId, final String name, final long value)
  {
    if (m_configuration == null) {
      initializeConfiguration();
    }

    final ConfigurationEntity entity = new ConfigurationEntity(contextId, name, ConfigValueType.LONG);

    entity.setLongValue(value);

    m_manager.merge(entity);

    m_configuration.put(contextId + ":" + name, value);
  }

  public void setStringValue(final String contextId, final String name, final String value)
  {
    if (m_configuration == null) {
      initializeConfiguration();
    }

    final ConfigurationEntity entity = new ConfigurationEntity(contextId, name, ConfigValueType.STRING);

    entity.setStringValue(value);

    m_manager.merge(entity);

    m_configuration.put(contextId + ":" + name, value);
  }

  private synchronized void initializeConfiguration()
  {
    m_configuration = Collections.synchronizedMap(new HashMap<String, Object>());

    final Query query = m_manager.createQuery("from " + ConfigurationEntity.class.getName());

    for (final Object row : query.getResultList()) {
      final ConfigurationEntity entity = (ConfigurationEntity) row;

      switch (entity.getValueType()) {
        case BOOLEAN:
          m_configuration.put(entity.getContextId() + ":" + entity.getName(), entity.getBooleanValue());
          break;
        case LONG:
          m_configuration.put(entity.getContextId() + ":" + entity.getName(), entity.getLongValue());
          break;
        case STRING:
          m_configuration.put(entity.getContextId() + ":" + entity.getName(), entity.getStringValue());
          break;
      }
    }
  }

}
