package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import de.objectcode.time4u.server.ejb.seam.api.IProjectServiceLocal;
import de.objectcode.time4u.server.web.ui.util.ProjectTaskSelection;

@Name("admin.moveProjectDataController")
@Scope(ScopeType.CONVERSATION)
public class MoveProjectDataController
{
  public static final String VIEW_ID = "/admin/moveProjectData.xhtml";

  private ProjectTaskSelection m_fromProject;
  private ProjectTaskSelection m_toProject;

  @In("ProjectService")
  IProjectServiceLocal m_projectService;

  @Begin(join = true)
  public String enter()
  {
    m_fromProject = new ProjectTaskSelection(VIEW_ID, m_projectService);
    m_toProject = new ProjectTaskSelection(VIEW_ID, m_projectService);

    return VIEW_ID;
  }

  public ProjectTaskSelection getFromProject()
  {
    return m_fromProject;
  }

  public ProjectTaskSelection getToProject()
  {
    return m_toProject;
  }
}
