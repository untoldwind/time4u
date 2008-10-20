package de.objectcode.time4u.server.web.ui.user;

import java.util.HashMap;
import java.util.Map;

import javax.faces.convert.Converter;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IReportServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ColumnType;
import de.objectcode.time4u.server.ejb.seam.api.report.DayInfoProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.GroupByDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.PersonProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.ProjectProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.TaskProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.WorkItemProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.WorkItemReportDefinition;
import de.objectcode.time4u.server.web.ui.converter.DateConverter;
import de.objectcode.time4u.server.web.ui.converter.StringArrayConverter;
import de.objectcode.time4u.server.web.ui.converter.StringConverter;
import de.objectcode.time4u.server.web.ui.converter.TimeConverter;

@Name("user.monthReportController")
@Scope(ScopeType.CONVERSATION)
public class MonthReportController
{
  public static enum Variant
  {
    FLAT("Flat"),
    GROUPBY_PERSON("GroupBy Person"),
    GROUPBY_DATE_PERSON("GroupBy Date/Person");

    private String m_label;

    private Variant(final String label)
    {
      m_label = label;
    }

    public String getLabel()
    {
      return m_label;
    }

    public void setLabel(final String label)
    {
      m_label = label;
    }
  }

  public static final String VIEW_ID = "/user/monthReport.xhtml";

  public static final String RESULT_VIEW_ID = "/user/monthReportResult.xhtml";

  @In("ReportService")
  IReportServiceLocal m_reportService;

  Variant m_variant = Variant.FLAT;
  MonthBean m_selectedMonth = new MonthBean();

  ReportResult m_reportResult;

  Map<ColumnType, Converter> m_converters;

  public MonthReportController()
  {
    m_converters = new HashMap<ColumnType, Converter>();
    m_converters.put(ColumnType.TIME, new TimeConverter());
    m_converters.put(ColumnType.DATE, new DateConverter());
    m_converters.put(ColumnType.NAME, new StringConverter());
    m_converters.put(ColumnType.NAME_ARRAY, new StringArrayConverter());
    m_converters.put(ColumnType.DESCRIPTION, new StringConverter());
  }

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

  public Variant getVariant()
  {
    return m_variant;
  }

  public void setVariant(final Variant variant)
  {
    m_variant = variant;
  }

  public Variant[] getVariants()
  {
    return Variant.values();
  }

  public ReportResult getReportResult()
  {
    return m_reportResult;
  }

  public Converter getConverter(final ColumnType type)
  {
    return m_converters.get(type);
  }

  public String generate()
  {
    final WorkItemReportDefinition definition = new WorkItemReportDefinition();

    switch (m_variant) {
      case FLAT:
        definition.setFilter(DateRangeFilter.filterMonth(m_selectedMonth.getYear(), m_selectedMonth.getMonth()));
        definition.addProjection(PersonProjection.NAME);
        definition.addProjection(DayInfoProjection.DATE);
        definition.addProjection(ProjectProjection.PATH);
        definition.addProjection(TaskProjection.NAME);
        definition.addProjection(WorkItemProjection.BEGIN);
        definition.addProjection(WorkItemProjection.END);
        definition.addProjection(WorkItemProjection.DURATION);
        definition.addProjection(WorkItemProjection.COMMENT);
        break;
      case GROUPBY_PERSON:
        definition.setFilter(DateRangeFilter.filterMonth(m_selectedMonth.getYear(), m_selectedMonth.getMonth()));
        definition.addProjection(DayInfoProjection.DATE);
        definition.addProjection(ProjectProjection.PATH);
        definition.addProjection(TaskProjection.NAME);
        definition.addProjection(WorkItemProjection.BEGIN);
        definition.addProjection(WorkItemProjection.END);
        definition.addProjection(WorkItemProjection.DURATION);
        definition.addProjection(WorkItemProjection.COMMENT);
        definition.addGroupByDefinition(new GroupByDefinition(PersonProjection.ID, PersonProjection.NAME));
        break;
      case GROUPBY_DATE_PERSON:
        definition.setFilter(DateRangeFilter.filterMonth(m_selectedMonth.getYear(), m_selectedMonth.getMonth()));
        definition.addProjection(ProjectProjection.PATH);
        definition.addProjection(TaskProjection.NAME);
        definition.addProjection(WorkItemProjection.BEGIN);
        definition.addProjection(WorkItemProjection.END);
        definition.addProjection(WorkItemProjection.DURATION);
        definition.addProjection(WorkItemProjection.COMMENT);
        definition.addGroupByDefinition(new GroupByDefinition(DayInfoProjection.DATE, DayInfoProjection.DATE));
        definition.addGroupByDefinition(new GroupByDefinition(PersonProjection.ID, PersonProjection.NAME));
        break;
    }

    m_reportResult = m_reportService.generateReport(definition);

    return RESULT_VIEW_ID;
  }
}
