package de.objectcode.time4u.server.web.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.convert.Converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IReportServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.io.XMLIO;
import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ColumnType;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.web.ui.converter.DateConverter;
import de.objectcode.time4u.server.web.ui.converter.StringArrayConverter;
import de.objectcode.time4u.server.web.ui.converter.StringConverter;
import de.objectcode.time4u.server.web.ui.converter.TimeConverter;

@Name("user.reportController")
@Scope(ScopeType.CONVERSATION)
public class ReportController
{
  private final static Log LOG = LogFactory.getLog(ReportController.class);

  public static final String VIEW_ID = "/user/report.xhtml";

  public static final String RESULT_VIEW_ID = "/user/reportResult.xhtml";

  @In("ReportService")
  IReportServiceLocal m_reportService;

  @In(value = "user.reportDefinition", required = false)
  @Out("user.reportDefinition")
  BaseReportDefinition m_reportDefinition;

  @In(value = "user.reportParameters", required = false)
  @Out("user.reportParameters")
  List<BaseParameterValue> m_reportParameters;

  @Out(value = "user.reportResult", required = false)
  ReportResult m_reportResult;

  @Out("user.reportConverters")
  Map<ColumnType, Converter> m_converters;

  public ReportController()
  {
    m_converters = new HashMap<ColumnType, Converter>();
    m_converters.put(ColumnType.TIME, new TimeConverter());
    m_converters.put(ColumnType.DATE, new DateConverter());
    m_converters.put(ColumnType.NAME, new StringConverter());
    m_converters.put(ColumnType.NAME_ARRAY, new StringArrayConverter());
    m_converters.put(ColumnType.DESCRIPTION, new StringConverter());
  }

  @Begin(join = true)
  public String enter(final String reportResource)
  {
    try {
      m_reportDefinition = XMLIO.INSTANCE.read(getClass().getClassLoader().getResourceAsStream(
          "reports/" + reportResource + ".xml"));
      m_reportParameters = new ArrayList<BaseParameterValue>();

      for (final ReportParameterDefinition parameterDefinition : m_reportDefinition.getParameters()) {
        m_reportParameters.add(parameterDefinition.newValueInstance());
      }
    } catch (final Exception e) {
      LOG.error("Exception", e);
    }

    return VIEW_ID;
  }

  public String back()
  {
    return VIEW_ID;
  }

  public String generate()
  {
    final Map<String, BaseParameterValue> parameters = new HashMap<String, BaseParameterValue>();

    for (final BaseParameterValue parameter : m_reportParameters) {
      parameters.put(parameter.getName(), parameter);
    }

    m_reportResult = m_reportService.generateReport(m_reportDefinition, parameters);

    return RESULT_VIEW_ID;
  }
}
