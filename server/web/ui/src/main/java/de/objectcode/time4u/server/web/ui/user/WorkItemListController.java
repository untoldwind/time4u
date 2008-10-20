package de.objectcode.time4u.server.web.ui.user;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IWorkItemServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.WorkItemData;

@Name("user.workItemListController")
@Scope(ScopeType.CONVERSATION)
public class WorkItemListController
{
  @In("WorkItemService")
  IWorkItemServiceLocal m_workItemService;

  int m_month;
  int m_year;

  @Create
  public void initialize()
  {
    final Calendar calendar = Calendar.getInstance();

    m_month = calendar.get(Calendar.MONTH) + 1;
    m_year = calendar.get(Calendar.YEAR);
  }

  public int getMonth()
  {
    return m_month;
  }

  public void setMonth(final int month)
  {
    m_month = month;
  }

  public int getYear()
  {
    return m_year;
  }

  public void setYear(final int year)
  {
    m_year = year;
  }

  public List<WorkItemData> getWorkItems()
  {
    final Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(m_year, m_month - 1, 1, 0, 0, 0);
    final Date from = new Date(calendar.getTimeInMillis());
    calendar.add(Calendar.MONTH, 1);
    final Date until = new Date(calendar.getTimeInMillis());

    return m_workItemService.getWorkItemData(from, until);
  }
}
