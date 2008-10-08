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

  MonthBean m_selectedMonth;

  @Create
  public void initialize()
  {
    final Calendar calendar = Calendar.getInstance();

    m_selectedMonth = new MonthBean(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
  }

  public MonthBean getSelectedMonth()
  {
    return m_selectedMonth;
  }

  public List<WorkItemData> getWorkItems()
  {
    final Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(m_selectedMonth.getYear(), m_selectedMonth.getMonth() - 1, 1, 0, 0, 0);
    final Date from = new Date(calendar.getTimeInMillis());
    calendar.add(Calendar.MONTH, 1);
    final Date until = new Date(calendar.getTimeInMillis());

    return m_workItemService.getWorkItemData(from, until);
  }
}
