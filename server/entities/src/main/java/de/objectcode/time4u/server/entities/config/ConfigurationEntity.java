package de.objectcode.time4u.server.entities.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "T4U_CONFIGURATION")
@IdClass(ConfigurationKey.class)
public class ConfigurationEntity
{
  private String m_contextId;
  private String m_name;
  private ConfigValueType m_valueType;
  private Boolean m_booleanValue;
  private Long m_longValue;
  private String m_stringValue;
  private String m_xmlValue;

  /**
   * Default constructor for hibernate.
   */
  protected ConfigurationEntity()
  {
  }

  public ConfigurationEntity(final String contextId, final String name, final ConfigValueType valueType)
  {
    m_contextId = contextId;
    m_name = name;
    m_valueType = valueType;
  }

  @Id
  @Column(length = 36)
  public String getContextId()
  {
    return m_contextId;
  }

  public void setContextId(final String contextId)
  {
    m_contextId = contextId;
  }

  @Id
  @Column(length = 100)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Type(type = "de.objectcode.time4u.server.entities.util.GenericEnumUserType", parameters = {
      @Parameter(name = "enumClass", value = "de.objectcode.time4u.server.entities.config.ConfigValueType"),
      @Parameter(name = "identifierMethod", value = "getCode"), @Parameter(name = "valueOfMethod", value = "forCode") })
  @Column(name = "value_type", nullable = false)
  public ConfigValueType getValueType()
  {
    return m_valueType;
  }

  public void setValueType(final ConfigValueType valueType)
  {
    m_valueType = valueType;
  }

  @Column(name = "boolean_value")
  public Boolean getBooleanValue()
  {
    return m_booleanValue;
  }

  public void setBooleanValue(final Boolean booleanValue)
  {
    m_booleanValue = booleanValue;
  }

  @Column(name = "long_value")
  public Long getLongValue()
  {
    return m_longValue;
  }

  public void setLongValue(final Long longValue)
  {
    m_longValue = longValue;
  }

  @Column(name = "string_value", length = 400)
  public String getStringValue()
  {
    return m_stringValue;
  }

  public void setStringValue(final String stringValue)
  {
    m_stringValue = stringValue;
  }

  @Lob
  @Column(name = "xml_value")
  public String getXmlValue()
  {
    return m_xmlValue;
  }

  public void setXmlValue(final String xmlValue)
  {
    m_xmlValue = xmlValue;
  }

}
