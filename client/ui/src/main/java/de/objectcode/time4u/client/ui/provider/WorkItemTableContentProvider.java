package de.objectcode.time4u.client.ui.provider;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.IWorkItemRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.WorkItem;

public class WorkItemTableContentProvider implements IStructuredContentProvider
{
  private final IWorkItemRepository m_workItemRepository;

  public WorkItemTableContentProvider(final IWorkItemRepository workItemRepository)
  {
    m_workItemRepository = workItemRepository;
  }

  public Object[] getElements(final Object inputElement)
  {
    if (inputElement instanceof CalendarDay) {
      final CalendarDay calendarDay = (CalendarDay) inputElement;

      try {
        final DayInfo dayInfo = m_workItemRepository.getDayInfo(calendarDay);
        final Collection<WorkItem> workItems = dayInfo.getWorkItems();

        if (workItems != null) {
          return workItems.toArray();
        }
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }
    }

    return new Object[0];
  }

  public void dispose()
  {
  }

  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
  {
  }
}
