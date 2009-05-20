package de.objectcode.time4u.server.web.ui.user;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.convert.Converter;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IReportServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.report.ColumnDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ColumnType;
import de.objectcode.time4u.server.ejb.seam.api.report.CrossTableResult;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.ValueLabelPair;
import de.objectcode.time4u.server.ejb.seam.api.report.CrossTableResult.CrossTableRow;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.web.ui.converter.DateConverter;
import de.objectcode.time4u.server.web.ui.converter.IntegerConverter;
import de.objectcode.time4u.server.web.ui.converter.StringArrayConverter;
import de.objectcode.time4u.server.web.ui.converter.StringConverter;
import de.objectcode.time4u.server.web.ui.converter.TimeConverter;
import de.objectcode.time4u.server.web.ui.converter.TimestampConverter;

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

  @Out("user.interactiveCrossTable")
  private CrossTableResult m_crossTable;

  @Out("user.reportResult")
  private ReportResult m_reportResult;

  @Out("user.reportConverters")
  Map<ColumnType, Converter> m_converters;

  public InteractiveReportController()
  {
    m_converters = new HashMap<ColumnType, Converter>();
    m_converters.put(ColumnType.TIME, new TimeConverter());
    m_converters.put(ColumnType.DATE, new DateConverter());
    m_converters.put(ColumnType.NAME, new StringConverter());
    m_converters.put(ColumnType.NAME_ARRAY, new StringArrayConverter());
    m_converters.put(ColumnType.INTEGER, new IntegerConverter());
    m_converters.put(ColumnType.DESCRIPTION, new StringConverter());
    m_converters.put(ColumnType.TIMESTAMP, new TimestampConverter());
  }

  public boolean isCrossTableReport()
  {
    return !m_interactiveFilter.isHasPerson();
  }

  public boolean isFlatReport()
  {
    return m_interactiveFilter.isHasPerson();
  }

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
    m_interactiveFilter.setPerson(person);

    return refresh();
  }

  public String clearFilterPerson()
  {
    m_interactiveFilter.setPerson(null);

    return refresh();
  }

  public String refresh()
  {
    if (m_interactiveFilter.isHasPerson()) {
      m_crossTable = new CrossTableResult(new ValueLabelPair[0], new CrossTableRow[0], new Object[0]);
      m_reportResult = m_reportService.generateReport(m_interactiveFilter.getReportDefinition(),
          new HashMap<String, BaseParameterValue>());
    } else {
      m_crossTable = m_reportService.generateProjectPersonCrossTable(m_interactiveFilter.getLastProjectId(),
          m_interactiveFilter.getFrom(), m_interactiveFilter.getUntil());
      m_reportResult = new ReportResult("<empty>", Collections.<ColumnDefinition> emptyList(), Collections
          .<ColumnDefinition> emptyList());
    }

    return VIEW_ID;
  }
}
