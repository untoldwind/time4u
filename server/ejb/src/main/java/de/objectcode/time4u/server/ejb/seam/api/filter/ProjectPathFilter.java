package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.Map;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

@XmlType(name = "project-path")
@XmlRootElement(name = "project-path")
public class ProjectPathFilter implements IFilter
{
  private static final long serialVersionUID = -8084207331936335305L;

  private String m_projectPath;

  @XmlAttribute(name = "project-path")
  public String getProjectPath()
  {
    return m_projectPath;
  }

  public void setProjectPath(final String projectPath)
  {
    m_projectPath = projectPath;
  }

  /**
   * {@inheritDoc}
   */
  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters)
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
      final Map<String, BaseParameterValue> parameters)
  {
    query.setParameter("projectPath", m_projectPath + "%");
  }
}
