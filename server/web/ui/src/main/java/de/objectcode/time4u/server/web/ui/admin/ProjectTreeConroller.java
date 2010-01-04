package de.objectcode.time4u.server.web.ui.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.component.UITree;
import org.richfaces.component.UITreeNode;
import org.richfaces.event.NodeExpandedEvent;

import de.objectcode.time4u.server.ejb.seam.api.IProjectServiceLocal;
import de.objectcode.time4u.server.entities.ProjectEntity;

@Name("admin.projectTreeController")
@Scope(ScopeType.CONVERSATION)
public class ProjectTreeConroller
{
  public static final String VIEW_ID = "/admin/projects.xhtml";

  public static final String DELETE_VIEW_ID = "/admin/deleteProject.xhtml";

  @In("ProjectService")
  IProjectServiceLocal m_projectService;

  ProjectBean m_selectedProject;
  boolean m_deleted = false;
  boolean m_onlyActive = false;

  Map<String, List<ProjectBean>> m_cache = new HashMap<String, List<ProjectBean>>();
  Set<String> expandedProjects = new HashSet<String>();

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
          projects = m_projectService.getRootProjects(m_deleted, m_onlyActive);
        } else {
          projects = m_projectService.getChildProjects(parentId, m_deleted, m_onlyActive);
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

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String select(final ProjectBean project)
  {
    m_selectedProject = project;
    return VIEW_ID;
  }

  public String confirmDeleteProject()
  {
    return DELETE_VIEW_ID;
  }

  public String deleteProject()
  {
    if (m_selectedProject != null) {
      m_projectService.deleteProject(m_selectedProject.getId());
      m_selectedProject = null;
      m_cache.clear();
    }
    return VIEW_ID;
  }

  public String undeleteProject()
  {
    if (m_selectedProject != null) {
      m_projectService.undeleteProject(m_selectedProject.getId());
      m_selectedProject = null;
      m_cache.clear();
    }
    return VIEW_ID;
  }

  public ProjectBean getSelectedProject()
  {
    return m_selectedProject;
  }

  public boolean isHasSelection()
  {
    return m_selectedProject != null;
  }

  public boolean adviseNodeOpened(final UITree tree)
  {
    if (tree.getRowData() != null && tree.getRowData() instanceof ProjectBean) {
      return expandedProjects.contains(((ProjectBean) tree.getRowData()).getId());
    }
    return false;
  }

  public void changeExpandListener(final NodeExpandedEvent event)
  {
    if (event.getComponent() instanceof UITreeNode) {
      final UITreeNode treeNode = (UITreeNode) event.getComponent();
      final UITree tree = treeNode.getUITree();

      if (tree.getRowData() != null && tree.getRowData() instanceof ProjectBean) {
        final String id = ((ProjectBean) tree.getRowData()).getId();

        if (expandedProjects.contains(id)) {
          expandedProjects.remove(id);
        } else {
          expandedProjects.add(id);
        }
      }
    }
  }

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    if (m_deleted != deleted) {
      m_cache.clear();
    }
    m_deleted = deleted;
  }

  public boolean isOnlyActive()
  {
    return m_onlyActive;
  }

  public void setOnlyActive(final boolean onlyActive)
  {
    if (m_onlyActive != onlyActive) {
      m_cache.clear();
    }
    m_onlyActive = onlyActive;
  }

  public class ProjectBean implements Serializable
  {
    private static final long serialVersionUID = -5934658867783123094L;

    String m_id;
    String m_parentId;
    String m_name;
    boolean m_active;
    boolean m_deleted;

    public ProjectBean(final ProjectEntity project)
    {
      m_id = project.getId();
      m_parentId = project.getParent() != null ? project.getParent().getId() : "";
      m_name = project.getName();
      m_active = project.isActive();
      m_deleted = project.isDeleted();
    }

    public String getId()
    {
      return m_id;
    }

    public String getParentId()
    {
      return m_parentId;
    }

    public String getName()
    {
      return m_name;
    }

    public boolean isActive()
    {
      return m_active;
    }

    public boolean isDeleted()
    {
      return m_deleted;
    }

  }
}
