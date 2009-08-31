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
    if (context.getMappedPersonId() != null) {
      if (entity.getOwnerIds() != null) {
        final int idx = entity.getOwnerIds().indexOf(context.getMappedPersonId());

        if (idx >= 0) {
          entity.getOwnerIds().remove(idx);
          entity.getOwnerIds().add(context.getRepository().getOwner().getId());
        }
      }
      if (entity.getMemberIds() != null) {
        final int idx = entity.getMemberIds().indexOf(context.getMappedPersonId());

        if (idx >= 0) {
          entity.getMemberIds().remove(idx);
          entity.getMemberIds().add(context.getRepository().getOwner().getId());
        }
      }
    }

    context.getRepository().getTeamRepository().storeTeam(entity, false);
  }
}