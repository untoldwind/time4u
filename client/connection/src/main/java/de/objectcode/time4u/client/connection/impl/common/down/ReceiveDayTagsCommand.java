package de.objectcode.time4u.client.connection.impl.common.down;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.impl.common.ISynchronizationCommand;
import de.objectcode.time4u.client.connection.impl.common.SynchronizationContext;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.DayTag;
import de.objectcode.time4u.server.api.data.FilterResult;

public class ReceiveDayTagsCommand implements ISynchronizationCommand
{
  public boolean shouldRun(final SynchronizationContext context)
  {
    return true;
  }

  public void execute(final SynchronizationContext context, final IProgressMonitor monitor) throws RepositoryException,
      ConnectionException
  {
    monitor.beginTask("Receive day tags", 1);

    try {
      final FilterResult<DayTag> dayTags = context.getWorkItemService().getDayTags();

      if (dayTags != null && dayTags.getResults() != null) {
        context.getRepository().getWorkItemRepository().storeDayTags(dayTags.getResults());
      } else {
        context.getRepository().getWorkItemRepository().storeDayTags(new ArrayList<DayTag>());
      }
    } finally {
      monitor.done();
    }
  }
}
