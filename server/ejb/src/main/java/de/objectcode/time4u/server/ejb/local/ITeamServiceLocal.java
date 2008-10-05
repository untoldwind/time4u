package de.objectcode.time4u.server.ejb.local;

import java.util.List;

import de.objectcode.time4u.server.entities.TeamEntity;

public interface ITeamServiceLocal
{
  List<TeamEntity> getTeams();
}
