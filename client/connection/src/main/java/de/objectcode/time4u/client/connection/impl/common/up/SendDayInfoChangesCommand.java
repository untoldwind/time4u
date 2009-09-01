package de.objectcode.time4u.client.connection.impl.common.up;

import java.util.ArrayList;
import java.util.Iterator;
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
    // Only send changes made by myself or by clients not know to the server
    filter.setLastModifiedByClient(context.getRepository().getClientId());
    filter.setMinRevision(minRevision);
    filter.setMaxRevision(maxRevision);

    final List<DayInfo> dayInfos = context.getRepository().getWorkItemRepository().getDayInfos(filter);

    if (context.getRootProject() != null) {
      final List<DayInfo> filteredDayInfos = new ArrayList<DayInfo>();
      final String rootProjectId = context.getRootProject().getId();

      for (final DayInfo dayInfo : dayInfos) {
        final Iterator<WorkItem> workItemIt = dayInfo.getWorkItems().iterator();

        while (workItemIt.hasNext()) {
          final WorkItem workItem = workItemIt.next();

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
            workItemIt.remove();
          }
        }
        filteredDayInfos.add(dayInfo);
      }

      return filteredDayInfos;
    }

    return dayInfos;
  }

  @Override
  protected void sendEntity(final SynchronizationContext context, final DayInfo entity) throws ConnectionException
  {
    context.getWorkItemService().storeDayInfo(entity);
  }
}
