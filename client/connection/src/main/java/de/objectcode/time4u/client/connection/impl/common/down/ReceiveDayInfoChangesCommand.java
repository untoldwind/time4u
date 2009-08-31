package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.WorkItem;
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
    if (context.getRootProject() != null) {
      if (entity.getWorkItems() == null) {
        entity.setWorkItems(new ArrayList<WorkItem>());
      }

      final String rootProjectId = context.getRootProject().getId();
      final DayInfo existingDayInfo = context.getRepository().getWorkItemRepository().getDayInfo(entity.getDay());

      if (existingDayInfo != null && existingDayInfo.getWorkItems() != null) {
        for (final WorkItem workItem : existingDayInfo.getWorkItems()) {
          ProjectSummary current = context.getRepository().getProjectRepository().getProjectSummary(
              workItem.getProjectId());
          while (current != null && !rootProjectId.equals(current.getParentId())) {
            if (current.getParentId() == null) {
              current = null;
            } else {
              current = context.getRepository().getProjectRepository().getProjectSummary(current.getParentId());
            }
          }
          if (current == null) {
            entity.getWorkItems().add(workItem);
          }
        }
      }
    }

    context.getRepository().getWorkItemRepository().storeDayInfo(entity, false);
  }
}
