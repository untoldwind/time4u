package de.objectcode.time4u.server.web.ui.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IProjectServiceLocal;
import de.objectcode.time4u.server.entities.ProjectEntity;

@Name("admin.projectTreeController")
@Scope(ScopeType.CONVERSATION)
public class ProjectTreeConroller
{
  @In("ProjectService")
  IProjectServiceLocal m_projectService;

  Map<String, List<ProjectBean>> m_cache = new HashMap<String, List<ProjectBean>>();

  public List<ProjectBean> getRootProjects()
  {
    return getChildProjects(null);
  }

  public List<ProjectBean> getChildProjects(final String parentId)
  {
    synchronized (m_cache) {
      List<ProjectBean> result = m_cache.get(parentId);

      if (result == null) {
        final List<ProjectEntity> projects;

        if (parentId == null) {
          projects = m_projectService.getRootProjects();
        } else {
          projects = m_projectService.getChildProjects(parentId);
        }

        result = new ArrayList<ProjectBean>();
        for (final ProjectEntity project : projects) {
          result.add(new ProjectBean(project));
        }

        m_cache.put(parentId, result);
      }

      return result;
    }
  }

  public static class ProjectBean implements Serializable
  {
    private static final long serialVersionUID = -5934658867783123094L;

    String m_id;
    String m_name;
    boolean m_active;

    ProjectBean(final ProjectEntity project)
    {
      m_id = project.getId();
      m_name = project.getName();
      m_active = project.isActive();
    }

    public String getId()
    {
      return m_id;
    }

    public String getName()
    {
      return m_name;
    }

    public boolean isActive()
    {
      return m_active;
    }

  }
}
