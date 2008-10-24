package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;

/**
 * Receive dayinfo changes from the server.
 * 
 * @author junglas
 */
public class ReceiveDayInfoChangesCommand extends BaseReceiveCommand<DayInfo>
{
  public ReceiveDayInfoChangesCommand()
  {
    super(EntityType.DAYINFO);
  }

  @Override
  protected List<DayInfo> receiveEntities(final SynchronizationContext context, final long minRevision,
      final long maxRevision) throws ConnectionException
  {
    final DayInfoFilter filter = new DayInfoFilter();
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    return context.getWorkItemService().getDayInfos(filter).getResults();
  }

  @Override
  protected void storeEntity(final SynchronizationContext context, final DayInfo entity) throws RepositoryException
  {
    context.getRepository().getWorkItemRepository().storeDayInfo(entity, false);
  }
}
