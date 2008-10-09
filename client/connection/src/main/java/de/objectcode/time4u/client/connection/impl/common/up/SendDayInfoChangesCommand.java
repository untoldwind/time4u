package de.objectcode.time4u.client.connection.impl.common.up;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;

/**
 * Send dayinfo changes to the server.
 * 
 * @author junglas
 */
public class SendDayInfoChangesCommand extends BaseSendCommand<DayInfo>
{
  public SendDayInfoChangesCommand()
  {
    super(EntityType.DAYINFO);
  }

  @Override
  protected List<DayInfo> queryEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws RepositoryException
  {
    final DayInfoFilter filter = new DayInfoFilter();
    // Only send changes made by myself
    filter.setLastModifiedByClient(context.getRepository().getClientId());
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    return context.getRepository().getWorkItemRepository().getDayInfos(filter);
  }

  @Override
  protected void sendEntity(final SynchronizationContext context, final DayInfo entity) throws ConnectionException
  {
    context.getWorkItemService().storeDayInfo(entity);
  }

}
