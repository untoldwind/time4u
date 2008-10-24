package de.objectcode.time4u.client.connection.impl.common.up;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.TimePolicy;
import de.objectcode.time4u.server.api.filter.TimePolicyFilter;

public class SendTimePolicyChangesCommand extends BaseSendCommand<TimePolicy>
{
  public SendTimePolicyChangesCommand()
  {
    super(EntityType.TIMEPOLICY);
  }

  @Override
  protected List<TimePolicy> queryEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws RepositoryException
  {
    final TimePolicyFilter filter = new TimePolicyFilter();
    // Only send changes made by myself
    filter.setLastModifiedByClient(context.getRepository().getClientId());

    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    return context.getRepository().getWorkItemRepository().getTimePolicies(filter);
  }

  @Override
  protected void sendEntity(final SynchronizationContext context, final TimePolicy entity) throws ConnectionException
  {
    context.getWorkItemService().storeTimePolicy(entity);
  }
}