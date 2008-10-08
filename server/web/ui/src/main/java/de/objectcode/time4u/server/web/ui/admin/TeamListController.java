package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;

import de.objectcode.time4u.server.ejb.seam.api.IPersonServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.ITeamServiceLocal;
import de.objectcode.time4u.server.entities.TeamEntity;

@Name("admin.teamListController")
@Scope(ScopeType.CONVERSATION)
public class TeamListController
{
  public static final String VIEW_ID = "/admin/teams.xhtml";

  @In("PersonService")
  IPersonServiceLocal m_personService;

  @In("TeamService")
  ITeamServiceLocal m_teamService;;

  TeamBean m_selectedTeam;

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String newTeam()
  {
    m_selectedTeam = new TeamBean(new TeamEntity(null, 0L, 0L, ""));

    return VIEW_ID;
  }

  public String select(final TeamEntity teamEntity)
  {
    m_selectedTeam = new TeamBean(m_teamService.getTeam(teamEntity.getId()));

    return VIEW_ID;
  }

  public TeamBean getSelectedTeam()
  {
    return m_selectedTeam;
  }

  public boolean isHasSelection()
  {
    return m_selectedTeam != null;
  }

  public String updateTeam()
  {
    if (m_selectedTeam != null) {
      m_selectedTeam.updateTeam(m_personService);
      m_teamService.storeTeam(m_selectedTeam.getTeam());
      StatusMessages.instance().add("Team information updated");
    }
    return VIEW_ID;
  }

}
