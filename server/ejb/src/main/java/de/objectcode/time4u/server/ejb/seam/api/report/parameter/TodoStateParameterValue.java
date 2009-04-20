package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.TodoStateFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterType;

public class TodoStateParameterValue extends BaseParameterValue
{
  private static final long serialVersionUID = 2152381811591513728L;

  private List<String> m_states;

  public TodoStateParameterValue(final String name, final String label)
  {
    super(name, label, ReportParameterType.TODO_STATE);

    m_states = new ArrayList<String>();
    for (final TodoState state : TodoState.values()) {
      m_states.add(String.valueOf(state.getCode()));
    }
  }

  public List<String> getStates()
  {
    return m_states;
  }

  public void setStates(final List<String> states)
  {
    m_states = states;
  }

  public TodoState[] getStateValues()
  {
    return TodoState.values();
  }

  @Override
  public IFilter getFilter()
  {
    final List<TodoState> states = new ArrayList<TodoState>();

    for (final String code : m_states) {
      states.add(TodoState.forCode(Integer.valueOf(code)));
    }
    return TodoStateFilter.filterTodoStates(states);
  }
}
