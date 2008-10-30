package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.filter.TeamFilter;

public class ReceiveTeamChangesCommand extends BaseReceiveCommand<Team>
{
  public ReceiveTeamChangesCommand()
  {
    super(EntityType.TEAM);
  }

  @Override
  protected List<Team> receiveEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws ConnectionException
  {
    final TeamFilter filter = new TeamFilter();
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);
    filter.setOrder(TeamFilter.Order.ID);

    return context.getTeamService().getTeams(filter).getResults();
  }

  @Override
  protected void storeEntity(final SynchronizationContext context, final Team entity) throws RepositoryException
  {
    context.getRepository().getTeamRepository().storeTeam(entity, false);
  }
}