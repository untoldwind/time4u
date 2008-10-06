package de.objectcode.time4u.server.web.ui.admin;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import de.objectcode.time4u.server.ejb.local.ITeamServiceLocal;
import de.objectcode.time4u.server.entities.TeamEntity;

@Name("admin.teamListController")
@Scope(ScopeType.CONVERSATION)
public class TeamListController
{
  public static final String VIEW_ID = "/admin/teams.xhtml";

  @In("TeamService")
  ITeamServiceLocal m_teamService;;

  @DataModel("admin.teamList")
  List<TeamEntity> m_teams;

  @DataModelSelection("admin.teamList")
  TeamEntity m_currentTeam;

  TeamEntity m_selectedTeam;

  @Factory("admin.teamList")
  public void getUserAccounts()
  {
    m_teams = m_teamService.getTeams();
  }

  @Begin
  @End
  public String enter()
  {
    return VIEW_ID;
  }

  public String select()
  {
    m_selectedTeam = m_currentTeam;

    return VIEW_ID;
  }

  public TeamEntity getSelectedTeam()
  {
    return m_selectedTeam;
  }
}
