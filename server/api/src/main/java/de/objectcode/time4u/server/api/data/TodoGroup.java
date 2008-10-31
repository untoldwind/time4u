package de.objectcode.time4u.server.api.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "todo-group")
@XmlRootElement(name = "todo-group")
public class TodoGroup extends TodoSummary
{
  private static final long serialVersionUID = -4564074138499769629L;

  /** Ids of the todos that are part of the group. */
  private List<String> m_partIds;
  /** Ids of teams that have interest in this todo. */
  private List<String> visibleToTeamIds;
  /** Ids of persons that have interest in this todo. */
  private List<String> visibleToPersonIds;
  /** Map of all meta properties of the team. */
  private Map<String, MetaProperty> m_metaProperties;

  public List<String> getPartIds()
  {
    return m_partIds;
  }

  public void setPartIds(final List<String> partIds)
  {
    m_partIds = partIds;
  }

  public List<String> getVisibleToTeamIds()
  {
    return visibleToTeamIds;
  }

  public void setVisibleToTeamIds(final List<String> visibleToTeamIds)
  {
    this.visibleToTeamIds = visibleToTeamIds;
  }

  public List<String> getVisibleToPersonIds()
  {
    return visibleToPersonIds;
  }

  public void setVisibleToPersonIds(final List<String> visibleToPersonIds)
  {
    this.visibleToPersonIds = visibleToPersonIds;
  }

  public Map<String, MetaProperty> getMetaProperties()
  {
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
}
