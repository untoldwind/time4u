package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

/**
 * Team DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "team")
public class Team extends TeamSummary
{
  private static final long serialVersionUID = -2393342879169250933L;

  /** List of server ids of all persons owning/administrating the team. */
  private List<String> m_ownerIds;
  /** List of server ids of all team members (person). */
  private List<String> m_memberIds;
  /** Map of all meta properties of the team. */
  private Map<String, MetaProperty> m_metaProperties;

  public List<String> getOwnerIds()
  {
    return m_ownerIds;
  }

  public void setOwnerIds(final List<String> ownerIds)
  {
    m_ownerIds = ownerIds;
  }

  public List<String> getMemberIds()
  {
    return m_memberIds;
  }

  public void setMemberIds(final List<String> memberIds)
  {
    m_memberIds = memberIds;
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

}
