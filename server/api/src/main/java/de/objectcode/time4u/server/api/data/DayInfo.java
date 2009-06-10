package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * DayInfo DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "day-info")
@XmlRootElement(name = "day-info")
public class DayInfo extends DayInfoSummary
{
  private static final long serialVersionUID = -2048583139605476186L;

  /** The time contingents of this day. */
  private Map<TimeContingent, Integer> m_timeContingents;
  /** All tags of this day */
  private Set<String> m_tags;
  /** All workitems of the day. */
  private List<WorkItem> m_workItems;
  /** Map of all meta properties of the dayinfo. */
  private Map<String, MetaProperty> m_metaProperties;

  public Map<TimeContingent, Integer> getTimeContingents()
  {
    return m_timeContingents;
  }

  public void setTimeContingents(final Map<TimeContingent, Integer> timeContingents)
  {
    m_timeContingents = timeContingents;
  }

  public Set<String> getTags()
  {
    return m_tags;
  }

  public void setTags(final Set<String> tags)
  {
    m_tags = tags;
  }

  public List<WorkItem> getWorkItems()
  {
    return m_workItems;
  }

  public void setWorkItems(final List<WorkItem> workItems)
  {
    m_workItems = workItems;
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
