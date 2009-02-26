package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Project DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "project")
@XmlRootElement(name = "project")
public class Project extends ProjectSummary
{
  private static final long serialVersionUID = 7628044489809504348L;

  /** Project description. */
  private String m_description;
  /** Map of all meta properties of the project. */
  private Map<String, MetaProperty> m_metaProperties;

  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  public Map<String, MetaProperty> getMetaProperties()
  {
    if (m_metaProperties == null) {
      return Collections.emptyMap();
    }
    return m_metaProperties;
  }

  public void setMetaProperties(final Map<String, MetaProperty> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  public void setMetaProperty(final MetaProperty metaProperty)
  {
    if (m_metaProperties == null) {
      m_metaProperties = new HashMap<String, MetaProperty>();
    }
    m_metaProperties.put(metaProperty.getName(), metaProperty);
  }

  public MetaProperty getMetaProperty(final String name)
  {
    if (m_metaProperties != null) {
      return m_metaProperties.get(name);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("Project(");
    buffer.append("super=").append(super.toString());
    buffer.append(", description=").append(m_description);
    buffer.append(")");
    return buffer.toString();
  }
}
