package de.objectcode.time4u.server.web.ui.user;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IReportServiceLocal;

@Name("user.monthReportController")
@Scope(ScopeType.CONVERSATION)
public class MonthReportController
{
  public static final String VIEW_ID = "/user/monthReport.xhtml";

  @In("ReportService")
  IReportServiceLocal m_reportService;

  MonthBean m_selectedMonth = new MonthBean();

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public MonthBean getSelectedMonth()
  {
    return m_selectedMonth;
  }

  public void setSelectedMonth(final MonthBean selectedMonth)
  {
    m_selectedMonth = selectedMonth;
  }

  public MonthEnum[] getMonthValues()
  {
    return MonthEnum.values();
  }
}
