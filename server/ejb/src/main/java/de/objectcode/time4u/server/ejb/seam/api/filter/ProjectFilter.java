package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.Map;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

@XmlType(name = "project")
@XmlRootElement(name = "project")
public class ProjectFilter implements IFilter
{
  private static final long serialVersionUID = 867092515665550531L;

  private String m_projectId;

  public ProjectFilter()
  {
  }

  public ProjectFilter(final String projectId)
  {
    m_projectId = projectId;
  }

  @XmlAttribute(name = "project-id")
  public String getProjectId()
  {
    return m_projectId;
  }

  public void setProjectId(final String projectId)
  {
    m_projectId = projectId;
  }

  /**
   * {@inheritDoc}
   */
  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters)
  {
    switch (entityType) {
      case WORKITEM:
        return "(w.project.id = :projectId)";
      case TODO:
        return "(t.task.project.id = :projectId)";
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
    query.setParameter("projectId", m_projectId);
  }

  public static ProjectFilter filterProject(final String projectId)
  {
    return new ProjectFilter(projectId);
  }
}
