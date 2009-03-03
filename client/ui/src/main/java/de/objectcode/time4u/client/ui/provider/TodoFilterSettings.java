package de.objectcode.time4u.client.ui.provider;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.Set;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class TodoFilterSettings
{
  boolean m_unassigned = true;
  boolean m_assignedToMe = true;
  boolean m_assignedToOther = true;
  Set<TodoState> m_states = EnumSet.allOf(TodoState.class);
  Integer m_hideCreatedOlderThan;
  Integer m_hideCompletedOlderThan;
  boolean m_onlyVisible = true;

  public boolean isUnassigned()
  {
    return m_unassigned;
  }

  public void setUnassigned(final boolean unassigned)
  {
    m_unassigned = unassigned;
  }

  public boolean isAssignedToMe()
  {
    return m_assignedToMe;
  }

  public void setAssignedToMe(final boolean assignedToMe)
  {
    m_assignedToMe = assignedToMe;
  }

  public boolean isAssignedToOther()
  {
    return m_assignedToOther;
  }

  public void setAssignedToOther(final boolean assignedToOther)
  {
    m_assignedToOther = assignedToOther;
  }

  public Set<TodoState> getStates()
  {
    return m_states;
  }

  public void setStates(final Set<TodoState> states)
  {
    m_states = states;
  }

  public Integer getHideCreatedOlderThan()
  {
    return m_hideCreatedOlderThan;
  }

  public void setHideCreatedOlderThan(final Integer hideCreatedOlderThan)
  {
    m_hideCreatedOlderThan = hideCreatedOlderThan;
  }

  public Integer getHideCompletedOlderThan()
  {
    return m_hideCompletedOlderThan;
  }

  public void setHideCompletedOlderThan(final Integer hideCompletedOlderThan)
  {
    m_hideCompletedOlderThan = hideCompletedOlderThan;
  }

  public boolean isOnlyVisible()
  {
    return m_onlyVisible;
  }

  public void setOnlyVisible(final boolean onlyVisible)
  {
    m_onlyVisible = onlyVisible;
  }

  public void apply(final TodoFilter filter)
  {
    if (m_unassigned && m_assignedToMe && m_assignedToOther) {
      filter.setAssignmentFilter(null);
    } else {
      filter.setAssignmentFilter(new TodoFilter.AssignmentFilter(m_unassigned, m_assignedToMe, m_assignedToOther,
          RepositoryFactory.getRepository().getOwner().getId()));
    }
    if (m_states.size() == TodoState.values().length) {
      filter.setTodoStates(null);
    } else {
      filter.setTodoStates(m_states.toArray(new TodoState[m_states.size()]));
    }
    if (m_hideCreatedOlderThan != null) {
      final Calendar now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 0);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.MILLISECOND, 0);
      now.add(Calendar.DAY_OF_MONTH, -m_hideCreatedOlderThan);
      filter.setCreatedAtGe(now.getTime());
    } else {
      filter.setCreatedAtGe(null);
    }
    if (m_hideCompletedOlderThan != null) {
      final Calendar now = Calendar.getInstance();
      now.set(Calendar.HOUR_OF_DAY, 0);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.MILLISECOND, 0);
      now.add(Calendar.DAY_OF_MONTH, -m_hideCompletedOlderThan);
      filter.setCompletedAtGe(now.getTime());
    } else {
      filter.setCompletedAtGe(null);
    }
  }
}
