package de.objectcode.time4u.server.web.ui.admin;

import java.util.List;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;

import de.objectcode.time4u.server.ejb.local.ITeamServiceLocal;
import de.objectcode.time4u.server.entities.TeamEntity;

@Name("admin.teamListController")
public class TeamListController
{
  public static final String VIEW_ID = "/admin/teams.xhtml";

  @In("TeamService")
  ITeamServiceLocal m_teamService;;

  @DataModel("admin.teamList")
  List<TeamEntity> m_teams;

  @Factory("admin.teamList")
  public List<TeamEntity> getUserAccounts()
  {
    return m_teamService.getTeams();
  }

  public String enter()
  {
    return VIEW_ID;
  }

}
