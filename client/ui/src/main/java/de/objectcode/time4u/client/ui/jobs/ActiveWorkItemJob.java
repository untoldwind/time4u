package de.objectcode.time4u.client.ui.jobs;

import java.util.Calendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.WorkItem;

public class ActiveWorkItemJob extends Job
{
  public ActiveWorkItemJob()
  {
    super("WorkItem Job");
  }

  @Override
  public boolean shouldRun()
  {
    if (RepositoryFactory.getRepository() == null) {
      return false;
    }
    try {
      return RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem() != null;
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
    return false;
  }

  @Override
  public boolean shouldSchedule()
  {
    if (RepositoryFactory.getRepository() == null) {
      return false;
    }
    try {
      return RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem() != null;
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IStatus run(final IProgressMonitor monitor)
  {
    final IRepository repository = RepositoryFactory.getRepository();

    if (repository != null) {
      try {
        final WorkItem workItem = repository.getWorkItemRepository().getActiveWorkItem();

        if (workItem != null) {
          final Calendar calendar = Calendar.getInstance();
          final CalendarDay day = new CalendarDay(calendar);

          final int hour = calendar.get(Calendar.HOUR_OF_DAY);
          final int minute = calendar.get(Calendar.MINUTE);
          final int end = 3600 * hour + 60 * minute;

          if (workItem.getDay().equals(day)) {
            if (workItem.getEnd() != end) {
              workItem.setEnd(end);
              repository.getWorkItemRepository().storeWorkItem(workItem);
            }
          } else {
            workItem.setEnd(24 * 3600);

            repository.getWorkItemRepository().storeWorkItem(workItem);

            final WorkItem nextActive = new WorkItem();

            nextActive.setDay(day);
            nextActive.setProjectId(workItem.getProjectId());
            nextActive.setTaskId(workItem.getTaskId());
            nextActive.setComment(workItem.getComment());
            nextActive.setBegin(0);
            nextActive.setEnd(end);

            repository.getWorkItemRepository().storeWorkItem(nextActive);
            repository.getWorkItemRepository().setActiveWorkItem(nextActive);
          }
        }
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }
    }

    schedule(30 * 1000L);

    return Status.OK_STATUS;
  }

}
