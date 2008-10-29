package de.objectcode.time4u.server.api.data;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Helper class to encode filter results in XML.
 * 
 * The is a limitation in JBoss' web-service implementation that does not allow <tt>java.util.List</tt> results.
 * 
 * @author junglas
 * 
 * @param <T>
 */
@XmlType(name = "filter-result")
@XmlRootElement(name = "filter-result")
public class FilterResult<T> implements Serializable
{
  private static final long serialVersionUID = -8284477347654961022L;

  private List<T> results;

  public FilterResult()
  {
  }

  public FilterResult(final List<T> results)
  {
    this.results = results;
  }

  @XmlElementRefs( { @XmlElementRef(type = Project.class), @XmlElementRef(type = ProjectSummary.class),
      @XmlElementRef(type = Task.class), @XmlElementRef(type = TaskSummary.class),
      @XmlElementRef(type = DayInfo.class), @XmlElementRef(type = DayInfoSummary.class),
      @XmlElementRef(type = WeekTimePolicy.class), @XmlElementRef(type = DayTag.class),
      @XmlElementRef(type = PersonSummary.class), @XmlElementRef(type = Person.class),
      @XmlElementRef(type = TeamSummary.class), @XmlElementRef(type = Team.class),
      @XmlElementRef(type = TodoSummary.class), @XmlElementRef(type = Todo.class),
      @XmlElementRef(type = TodoGroup.class) })
  public List<T> getResults()
  {
    return results;
  }

  public void setResults(final List<T> results)
  {
    this.results = results;
  }

}
