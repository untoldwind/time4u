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

@XmlType(name = "task")
@XmlRootElement(name = "task")
public class TaskFilter implements IFilter
{
  private static final long serialVersionUID = -5766422605433132577L;

  private String m_taskId;
  private String m_taskIdExpression;

  public TaskFilter()
  {
  }

  public TaskFilter(final String taskId)
  {
    m_taskId = taskId;
  }

  @XmlAttribute(name = "task-id")
  public String getTaskId()
  {
    return m_taskId;
  }

  public void setTaskId(final String taskId)
  {
    m_taskId = taskId;
  }

  @XmlAttribute(name = "task-id-expression")
  public String getTaskIdExpression()
  {
    return m_taskIdExpression;
  }

  public void setTaskIdExpression(final String taskIdExpression)
  {
    m_taskIdExpression = taskIdExpression;
  }

  /**
   * {@inheritDoc}
   */
  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters,
      final ELContext context)
  {
    switch (entityType) {
      case WORKITEM:
        return "(w.task.id = :taskId)";
      case TODO:
        return "(t.task.id = :taskId)";
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
    if (m_taskIdExpression != null) {
      final ExpressionFactory factory = ReportEL.getExpressionFactory();

      query.setParameter("taskId", factory.createValueExpression(context, m_taskIdExpression, String.class).getValue(
          context));
    } else {
      query.setParameter("taskId", m_taskId);
    }
  }

}
