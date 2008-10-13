package de.objectcode.time4u.server.entities;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.MetaType;

/**
 * Abstract base class of all meta property entities.
 * 
 * @author junglas
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(MetaPropertyKey.class)
public abstract class BaseMetaPropertyEntity
{
  /** Name of the property */
  protected String m_name;
  /** Meta type of the property. */
  protected MetaType m_metaType;
  /** The id of the entity the meta property belongs too */
  protected String m_entityId;
  /** String value (if it is a string) */
  protected String m_strValue;
  /** Integer value (if it is an integer) */
  protected Integer m_intValue;
  /** Boolean value (if it is a boolean) */
  protected Boolean m_boolValue;
  /** Date/timestamp value (if it is a date/timestamp) */
  protected Date m_dateValue;

  @Id
  @Column(length = 200, nullable = false)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Id
  @Column(length = 36, nullable = false)
  public String getEntityId()
  {
    return m_entityId;
  }

  public void setEntityId(final String entityId)
  {
    m_entityId = entityId;
  }

  @Type(type = "de.objectcode.time4u.server.entities.util.GenericEnumUserType", parameters = {
      @Parameter(name = "enumClass", value = "de.objectcode.time4u.server.api.data.EntityType"),
      @Parameter(name = "identifierMethod", value = "getCode"), @Parameter(name = "valueOfMethod", value = "forCode") })
  @Column(name = "metaType", nullable = false)
  public MetaType getMetaType()
  {
    return m_metaType;
  }

  public void setMetaType(final MetaType metaType)
  {
    m_metaType = metaType;
  }

  @Column(name = "strValue", length = 1000, nullable = true)
  public String getStrValue()
  {
    return m_strValue;
  }

  public void setStrValue(final String strValue)
  {
    m_strValue = strValue;
  }

  @Column(name = "boolValue", nullable = true)
  public Boolean getBoolValue()
  {
    return m_boolValue;
  }

  public void setBoolValue(final Boolean boolValue)
  {
    m_boolValue = boolValue;
  }

  @Column(name = "dateValue", nullable = true)
  public Date getDateValue()
  {
    return m_dateValue;
  }

  public void setDateValue(final Date dateValue)
  {
    m_dateValue = dateValue;
  }

  @Column(name = "intValue", nullable = true)
  public Integer getIntValue()
  {
    return m_intValue;
  }

  public void setIntValue(final Integer intValue)
  {
    m_intValue = intValue;
  }

  public void fromDTO(final MetaProperty metaProperty)
  {
    m_name = metaProperty.getName();
    switch (MetaType.valueOf(metaProperty.getType())) {
      case STRING:
        m_strValue = metaProperty.getValue();
        break;
      case INTEGER:
        m_intValue = Integer.parseInt(metaProperty.getValue());
        break;
      case BOOLEAN:
        m_boolValue = Boolean.parseBoolean(metaProperty.getValue());
        break;
      case DATE:
        try {
          m_dateValue = MetaProperty.g_format.parse(metaProperty.getValue());
        } catch (final ParseException e) {
        }
        break;
      default:
        throw new RuntimeException("Unhandled meta type: " + m_metaType);
    }
  }

  public MetaProperty toDTO()
  {
    switch (m_metaType) {
      case STRING:
        return new MetaProperty(m_name, m_strValue);
      case INTEGER:
        return new MetaProperty(m_name, m_intValue);
      case BOOLEAN:
        return new MetaProperty(m_name, m_boolValue);
      case DATE:
        return new MetaProperty(m_name, m_dateValue);
    }
    throw new RuntimeException("Unhandled meta type: " + m_metaType);
  }
}
