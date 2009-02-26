package de.objectcode.time4u.client.ui.provider;

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
  Integer m_hideOderThan;

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

  public Integer getHideOderThan()
  {
    return m_hideOderThan;
  }

  public void setHideOderThan(final Integer hideOderThan)
  {
    m_hideOderThan = hideOderThan;
  }

  public void apply(final TodoFilter filter)
  {
    if (m_unassigned && m_assignedToMe && m_assignedToOther) {
      filter.setAssignmentFilter(null);
    } else {
      filter.setAssignmentFilter(new TodoFilter.AssignmentFilter(m_unassigned, m_assignedToMe, m_assignedToOther,
          RepositoryFactory.getRepository().getOwner().getId()));
    }
  }
}
