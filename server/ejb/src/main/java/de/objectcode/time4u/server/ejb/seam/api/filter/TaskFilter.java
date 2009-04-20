package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.Map;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

@XmlType(name = "task")
@XmlRootElement(name = "task")
public class TaskFilter implements IFilter
{
  private static final long serialVersionUID = -5766422605433132577L;

  private String m_taskId;

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

  /**
   * {@inheritDoc}
   */
  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters)
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
      final Map<String, BaseParameterValue> parameters)
  {
    query.setParameter("taskId", m_taskId);
  }

}
