package de.objectcode.time4u.server.web.ui.user;

import java.io.Serializable;
import java.util.Calendar;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IReportServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.report.CrossTableResult;
import de.objectcode.time4u.server.ejb.seam.api.report.ValueLabelPair;

@Name("user.interactiveReportController")
@Scope(ScopeType.CONVERSATION)
public class InteractiveReportController implements Serializable
{
  private static final long serialVersionUID = -7166535296179928941L;

  public static final String VIEW_ID = "/user/interactive.xhtml";

  @In("ReportService")
  IReportServiceLocal m_reportService;

  @In(required = false, value = "user.interactiveFilter")
  @Out("user.interactiveFilter")
  private InteractiveFilter m_interactiveFilter;

  @Out("user.interactiveReport")
  private CrossTableResult m_crossTable;

  @Begin(join = true)
  public String enter()
  {
    m_interactiveFilter = new InteractiveFilter();
    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    m_interactiveFilter.setFrom(calendar.getTime());

    calendar.add(Calendar.MONTH, 1);

    m_interactiveFilter.setUntil(calendar.getTime());

    return refresh();
  }

  public String back1Month()
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.setTime(m_interactiveFilter.getFrom());
    calendar.add(Calendar.MONTH, -1);
    m_interactiveFilter.setFrom(calendar.getTime());
    calendar.setTime(m_interactiveFilter.getUntil());
    calendar.add(Calendar.MONTH, -1);
    m_interactiveFilter.setUntil(calendar.getTime());

    return refresh();
  }

  public String forward1Month()
  {
    final Calendar calendar = Calendar.getInstance();

    calendar.setTime(m_interactiveFilter.getFrom());
    calendar.add(Calendar.MONTH, 1);
    m_interactiveFilter.setFrom(calendar.getTime());
    calendar.setTime(m_interactiveFilter.getUntil());
    calendar.add(Calendar.MONTH, 1);
    m_interactiveFilter.setUntil(calendar.getTime());

    return refresh();
  }

  public String clearFilterProject()
  {
    m_interactiveFilter.clearProject();

    return refresh();
  }

  public String setFilterProject(final ValueLabelPair project)
  {
    m_interactiveFilter.setProject(project);

    return refresh();
  }

  public String addFilterProject(final ValueLabelPair project)
  {
    m_interactiveFilter.addProject(project);

    return refresh();
  }

  public String setFilterPerson(final ValueLabelPair person)
  {
    return VIEW_ID;
  }

  public String refresh()
  {
    m_crossTable = m_reportService.generateProjectPersonCrossTable(m_interactiveFilter.getLastProjectId(),
        m_interactiveFilter.getFrom(), m_interactiveFilter.getUntil());

    return VIEW_ID;
  }
}
