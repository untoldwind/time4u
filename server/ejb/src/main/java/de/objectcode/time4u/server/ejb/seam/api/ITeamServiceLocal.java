package de.objectcode.time4u.server.ejb.seam.api;

import de.objectcode.time4u.server.entities.TeamEntity;

public interface ITeamServiceLocal
{
  void initTeams();

  TeamEntity getTeam(String id);

  void storeTeam(TeamEntity teamEntity);
}
