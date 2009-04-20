package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.ProjectFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterType;

public class ProjectParameterValue extends BaseParameterValue
{
  private static final long serialVersionUID = 2152381811591513728L;

  private final List<String> m_projectStack;

  public ProjectParameterValue(final String name, final String label)
  {
    super(name, label, ReportParameterType.PROJECT);

    m_projectStack = new ArrayList<String>();
  }

  public ProjectParameterValue(final String name, final String label, final String projectId)
  {
    super(name, label, ReportParameterType.PROJECT);

    m_projectStack = new ArrayList<String>();
  }

  public String getProjectId()
  {
    if (m_projectStack.size() > 0) {
      return m_projectStack.get(m_projectStack.size() - 1);
    }
    return null;
  }

  public void setProjectId(final String projectId)
  {
    if ("parent".equals(projectId)) {
      if (m_projectStack.size() > 0) {
        m_projectStack.remove(m_projectStack.size() - 1);
      }
    } else if (projectId != null
        && (m_projectStack.size() == 0 || !m_projectStack.get(m_projectStack.size() - 1).equals(projectId))) {
      m_projectStack.add(projectId);
    }
  }

  public List<String> getProjectStack()
  {
    return m_projectStack;
  }

  @Override
  public IFilter getFilter()
  {
    if (m_projectStack.size() > 0) {
      final String projectId = m_projectStack.get(m_projectStack.size() - 1);
      return ProjectFilter.filterProject(projectId);
    }
    return null;
  }

}
