package de.objectcode.time4u.server.web.ui.admin;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.web.ui.admin.ProjectTreeConroller.ProjectBean;

@Name("admin.moveProjectDataController")
@Scope(ScopeType.CONVERSATION)
public class MoveProjectDataController
{
  public static final String VIEW_ID = "/admin/moveProjectData.xhtml";

  private final List<String> m_fromProjectStack = new ArrayList<String>();
  private final List<String> m_toProjectStack = new ArrayList<String>();

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String selectProject(final ProjectBean project)
  {
    return VIEW_ID;
  }

  public String getFromProjectId()
  {
    if (m_fromProjectStack.size() > 0) {
      return m_fromProjectStack.get(m_fromProjectStack.size() - 1);
    }
    return null;
  }

  public void setFromProjectId(final String projectId)
  {
    if ("parent".equals(projectId)) {
      if (m_fromProjectStack.size() > 0) {
        m_fromProjectStack.remove(m_fromProjectStack.size() - 1);
      }
    } else if (projectId != null
        && (m_fromProjectStack.size() == 0 || !m_fromProjectStack.get(m_fromProjectStack.size() - 1).equals(projectId))) {
      m_fromProjectStack.add(projectId);
    }
  }

  public List<String> getFromProjectStack()
  {
    return m_fromProjectStack;
  }

  public String getToProjectId()
  {
    if (m_toProjectStack.size() > 0) {
      return m_toProjectStack.get(m_toProjectStack.size() - 1);
    }
    return null;
  }

  public void setToProjectId(final String projectId)
  {
    if ("parent".equals(projectId)) {
      if (m_toProjectStack.size() > 0) {
        m_toProjectStack.remove(m_toProjectStack.size() - 1);
      }
    } else if (projectId != null
        && (m_toProjectStack.size() == 0 || !m_toProjectStack.get(m_toProjectStack.size() - 1).equals(projectId))) {
      m_toProjectStack.add(projectId);
    }
  }

  public List<String> getToProjectStack()
  {
    return m_toProjectStack;
  }

}
