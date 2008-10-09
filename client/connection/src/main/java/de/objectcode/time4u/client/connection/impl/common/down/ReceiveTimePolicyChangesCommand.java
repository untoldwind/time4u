package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.TimePolicy;
import de.objectcode.time4u.server.api.filter.TimePolicyFilter;

public class ReceiveTimePolicyChangesCommand extends BaseReceiveCommand<TimePolicy>
{
  public ReceiveTimePolicyChangesCommand()
  {
    super(EntityType.TIMEPOLICY);
  }

  @Override
  protected List<TimePolicy> receiveEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws ConnectionException
  {
    final TimePolicyFilter filter = new TimePolicyFilter();
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    return context.getWorkItemService().getTimePolicies(filter).getResults();
  }

  @Override
  protected void storeEntity(final SynchronizationContext context, final TimePolicy entity) throws RepositoryException
  {
    context.getRepository().getWorkItemRepository().storeTimePolicy(entity, false);
  }
}
