package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.ejb.util.ReportEL;

@XmlType(name = "project-path")
@XmlRootElement(name = "project-path")
public class ProjectPathFilter implements IFilter
{
  private static final long serialVersionUID = -8084207331936335305L;

  private String m_projectPath;
  private String m_projectPathExpression;

  public ProjectPathFilter()
  {

  }

  public ProjectPathFilter(final String projectPath)
  {
    m_projectPath = projectPath;
  }

  @XmlAttribute(name = "project-path")
  public String getProjectPath()
  {
    return m_projectPath;
  }

  public void setProjectPath(final String projectPath)
  {
    m_projectPath = projectPath;
  }

  @XmlAttribute(name = "project-path-expression")
  public String getProjectPathExpression()
  {
    return m_projectPathExpression;
  }

  public void setProjectPathExpression(final String projectPathExpression)
  {
    m_projectPathExpression = projectPathExpression;
  }

  /**
   * {@inheritDoc}
   */
  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters,
      final ELContext context)
  {
    switch (entityType) {
      case WORKITEM:
        return "(w.project.parentKey like :projectPath)";
      case TODO:
        return "(t.task.project.parentKey like :projectPath)";
      default:
        throw new RuntimeException("PersonFilter not applicable for entity type: " + entityType);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setQueryParameters(final EntityType entityType, final Query query,
      final Map<String, BaseParameterValue> parameters, final ELContext context)
  {
    if (m_projectPathExpression != null) {
      final ExpressionFactory factory = ReportEL.getExpressionFactory();

      query.setParameter("projectPath", factory.createValueExpression(context, m_projectPathExpression, String.class)
          .getValue(context)
          + "%");
    } else {
      query.setParameter("projectPath", m_projectPath + "%");
    }
  }
}
