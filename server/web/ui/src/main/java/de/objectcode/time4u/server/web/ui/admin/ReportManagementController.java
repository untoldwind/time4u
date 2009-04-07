package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.entities.report.ReportDefinitionEntity;

@Name("admin.reportManagementController")
@Scope(ScopeType.CONVERSATION)
public class ReportManagementController
{
  public static final String VIEW_ID = "/admin/manageReports.xhtml";

  ReportDefinitionEntity m_selectedReport;
  int m_currentPage;

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String select(final ReportDefinitionEntity reportEntity)
  {
    m_selectedReport = reportEntity;

    return VIEW_ID;
  }

  public boolean isHasSelection()
  {
    return m_selectedReport != null;
  }

  public ReportDefinitionEntity getSelectedReport()
  {
    return m_selectedReport;
  }

  public int getCurrentPage()
  {
    return m_currentPage;
  }

  public void setCurrentPage(final int currentPage)
  {
    m_currentPage = currentPage;
  }
}
