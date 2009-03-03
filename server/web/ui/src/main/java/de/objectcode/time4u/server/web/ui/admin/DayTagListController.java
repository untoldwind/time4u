package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IWorkItemServiceLocal;
import de.objectcode.time4u.server.entities.DayTagEntity;

@Name("admin.dayTagListController")
@Scope(ScopeType.CONVERSATION)
public class DayTagListController
{
  public final static String VIEW_ID = "/admin/daytags.xhtml";

  DayTagEntity m_selectedDayTag;

  @In("WorkItemService")
  IWorkItemServiceLocal m_workItemService;

  public DayTagEntity getSelectedDayTag()
  {
    return m_selectedDayTag;
  }

  public void setSelectedDayTag(final DayTagEntity selectedDayTag)
  {
    m_selectedDayTag = selectedDayTag;
  }

  public boolean isHasSelection()
  {
    return m_selectedDayTag != null;
  }

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String select(final DayTagEntity dayTag)
  {
    m_selectedDayTag = dayTag;

    return VIEW_ID;
  }

  public String newDayTag()
  {
    m_selectedDayTag = new DayTagEntity();

    return VIEW_ID;
  }

  public String updateDayTag()
  {
    m_workItemService.storeDayTag(m_selectedDayTag);

    return VIEW_ID;
  }

  public String deleteDayTag()
  {
    m_workItemService.deleteDayTag(m_selectedDayTag);

    return VIEW_ID;
  }
}
