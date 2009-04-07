package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IReportManagementServiceLocal;
import de.objectcode.time4u.server.entities.report.ReportDefinitionEntity;

@Name("admin.reportManagementController")
@Scope(ScopeType.CONVERSATION)
public class ReportManagementController
{
  public static final String VIEW_ID = "/admin/manageReports.xhtml";

  @In("ReportManagementService")
  IReportManagementServiceLocal m_reportManagementService;

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

  public String updateReport()
  {
    if (m_selectedReport != null) {
      m_reportManagementService.storeReportDefinitionEntity(m_selectedReport);
    }

    return VIEW_ID;
  }

  public String newReport()
  {
    m_selectedReport = new ReportDefinitionEntity("", "", "", "", "");

    return VIEW_ID;
  }

  public String deleteReport()
  {
    if (m_selectedReport != null) {
      m_reportManagementService.deleteReportDeinfitionEntity(m_selectedReport.getId());
    }

    return VIEW_ID;
  }
}
