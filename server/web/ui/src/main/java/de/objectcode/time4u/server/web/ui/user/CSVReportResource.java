package de.objectcode.time4u.server.web.ui.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.core.ConversationPropagation;
import org.jboss.seam.core.Manager;
import org.jboss.seam.web.AbstractResource;
import org.jboss.seam.web.ServletContexts;

import de.objectcode.time4u.server.ejb.seam.api.report.ColumnDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;

@Scope(ScopeType.APPLICATION)
@Name("user.csvReportResource")
@BypassInterceptors
@Install(precedence = Install.APPLICATION)
@Startup
public class CSVReportResource extends AbstractResource
{
  private final static String DELIM = ";";
  private final static String FIELD = "\"";

  @Override
  public void getResource(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException
  {
    ServletLifecycle.beginRequest(request);
    try {
      ServletContexts.instance().setRequest(request);
      ConversationPropagation.instance().restoreConversationId(request.getParameterMap());
      Manager.instance().restoreConversation();
      ServletLifecycle.resumeConversation(request);

      final ReportResult reportResult = (ReportResult) Contexts.getConversationContext().get("user.reportResult");

      if (reportResult != null) {
        response.setContentType("text/csv;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        final ServletOutputStream out = response.getOutputStream();

        boolean first = true;

        for (final ColumnDefinition column : reportResult.getGroupByColumns()) {
          if (!first) {
            out.print(DELIM);
          }
          out.print(toCSVString(column.getHeader()));
          first = false;
        }
        for (final ColumnDefinition column : reportResult.getColumns()) {
          if (!first) {
            out.print(DELIM);
          }
          out.print(toCSVString(column.getHeader()));
          first = false;
        }
        out.println();

        // TODO

        out.close();
      }
    } finally {
      ServletLifecycle.endRequest(request);
    }
  }

  @Override
  public String getResourcePath()
  {
    return "/csvreport";
  }

  private String toCSVString(final String str)
  {
    return FIELD + str.replace(FIELD, FIELD + FIELD) + FIELD;
  }
}
