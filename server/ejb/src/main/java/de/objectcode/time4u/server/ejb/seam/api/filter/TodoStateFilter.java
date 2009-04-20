package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

@XmlType(name = "todo-state")
@XmlRootElement(name = "todo-state")
public class TodoStateFilter implements IFilter
{
  private static final long serialVersionUID = 800645720054710604L;

  private List<TodoState> m_states;

  public TodoStateFilter()
  {
  }

  private TodoStateFilter(final List<TodoState> states)
  {
    m_states = states;
  }

  @XmlElement(name = "state", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = TodoState.class)
  public List<TodoState> getStates()
  {
    return m_states;
  }

  public void setStates(final List<TodoState> states)
  {
    m_states = states;
  }

  /**
   * {@inheritDoc}
   */
  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters)
  {
    switch (entityType) {
      case TODO:
        if (m_states != null && m_states.size() > 0) {
          final StringBuffer buffer = new StringBuffer("(t.state in (");
          boolean first = true;
          for (final TodoState state : m_states) {
            if (!first) {
              buffer.append(",");
            }
            buffer.append(state.getCode());
            first = false;
          }
          buffer.append("))");
          return buffer.toString();
        }
        return "(t.state is not null)";
      default:
        throw new RuntimeException("TodoStateFilter not applicable for entity type: " + entityType);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setQueryParameters(final EntityType entityType, final Query query,
      final Map<String, BaseParameterValue> parameters)
  {
  }

  public static TodoStateFilter filterTodoStates(final List<TodoState> states)
  {
    return new TodoStateFilter(states);
  }
}
