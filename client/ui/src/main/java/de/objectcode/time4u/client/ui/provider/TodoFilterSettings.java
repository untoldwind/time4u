package de.objectcode.time4u.client.ui.provider;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class TodoFilterSettings
{
  boolean unassigned = true;
  boolean assignedToMe = true;
  boolean assignedToOther = true;

  public boolean isUnassigned()
  {
    return unassigned;
  }

  public void setUnassigned(final boolean unassigned)
  {
    this.unassigned = unassigned;
  }

  public boolean isAssignedToMe()
  {
    return assignedToMe;
  }

  public void setAssignedToMe(final boolean assignedToMe)
  {
    this.assignedToMe = assignedToMe;
  }

  public boolean isAssignedToOther()
  {
    return assignedToOther;
  }

  public void setAssignedToOther(final boolean assignedToOther)
  {
    this.assignedToOther = assignedToOther;
  }

  public void apply(final TodoFilter filter)
  {
    if (unassigned && assignedToMe && assignedToOther) {
      filter.setAssignmentFilter(null);
    } else {
      filter.setAssignmentFilter(new TodoFilter.AssignmentFilter(unassigned, assignedToMe, assignedToOther,
          RepositoryFactory.getRepository().getOwner().getId()));
    }
  }
}
