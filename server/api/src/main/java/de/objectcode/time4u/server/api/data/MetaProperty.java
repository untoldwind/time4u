package de.objectcode.time4u.server.api.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Generic meta property DTO object.
 * 
 * Almost all business objects of Time4U may have an arbitrary number of user-defined meta properties.
 * 
 * @author junglas
 */
@XmlType(name = "meta-property")
public class MetaProperty implements Serializable
{
  private static final long serialVersionUID = -218883908448368551L;
  public static final SimpleDateFormat g_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

  /** The name of the meta property */
  private String m_name;
  /** The type of the meta property */
  private MetaType m_type;
  /** The value of the meta property coded as string */
  private String m_valueAsString;

  public MetaProperty()
  {
  }

  public MetaProperty(final String name, final MetaType type, final Object value)
  {
    m_name = name;
    m_type = type;

    switch (type) {
      case BOOLEAN:
      case INTEGER:
      case STRING:
        m_valueAsString = value.toString();
        break;
      case DATE:
        m_valueAsString = g_format.format(value);
        break;
    }
  }

  public MetaProperty(final String name, final String type, final String value)
  {
    m_name = name;
    m_type = MetaType.valueOf(type);
    m_valueAsString = value;
  }

  public MetaProperty(final String name, final Boolean value)
  {
    m_name = name;
    m_type = MetaType.BOOLEAN;
    m_valueAsString = value.toString();
  }

  public MetaProperty(final String name, final String value)
  {
    m_name = name;
    m_type = MetaType.STRING;
    m_valueAsString = value;
  }

  public MetaProperty(final String name, final Integer value)
  {
    m_name = name;
    m_type = MetaType.INTEGER;
    m_valueAsString = value.toString();
  }

  public MetaProperty(final String name, final Date value)
  {
    m_name = name;
    m_type = MetaType.DATE;
    m_valueAsString = g_format.format(value);
  }

  @XmlAttribute
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @XmlAttribute
  public String getType()
  {
    return m_type.toString();
  }

  public void setType(final String type)
  {
    m_type = MetaType.valueOf(type);
  }

  @XmlAttribute
  public String getValue()
  {
    return m_valueAsString;
  }

  public void setValue(final String value)
  {
    m_valueAsString = value;
  }

}
