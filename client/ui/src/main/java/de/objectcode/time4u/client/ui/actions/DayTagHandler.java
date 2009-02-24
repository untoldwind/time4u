package de.objectcode.time4u.client.ui.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayTag;

public class DayTagHandler extends AbstractHandler
{

  public Object execute(final ExecutionEvent event) throws ExecutionException
  {
    final String dayTagName = event.getParameter("dayTag");

    if (dayTagName == null) {
      return null;
    }

    final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

    if (workbenchWindow == null) {
      return null;
    }

    final ISelectionService selectionService = workbenchWindow.getSelectionService();
    final ISelection selection = selectionService.getSelection();

    if (selection == null || !(selection instanceof IAdaptable)) {
      return null;
    }

    final CalendarDay calendarDay = (CalendarDay) ((IAdaptable) selection).getAdapter(CalendarDay.class);

    if (calendarDay == null) {
      return null;
    }

    try {
      final List<DayTag> dayTags = RepositoryFactory.getRepository().getWorkItemRepository().getDayTags();
      DayTag dayTag = null;

      for (final DayTag tag : dayTags) {
        if (dayTagName.equals(tag.getName())) {
          dayTag = tag;
          break;
        }
      }
      if (dayTag == null) {
        return null;
      }

      final DayInfo dayInfo = RepositoryFactory.getRepository().getWorkItemRepository().getDayInfo(calendarDay);
      final Set<String> currentTags = dayInfo != null ? dayInfo.getTags() : new HashSet<String>();

      if (!currentTags.isEmpty() && !currentTags.contains(dayTag.getName()) && dayTag.getRegularTime() != null
          && dayTag.getRegularTime() == dayInfo.getRegularTime()) {
        return null;
      }

      if (currentTags.contains(dayTag.getName())) {
        currentTags.remove(dayTag.getName());
      } else {
        currentTags.add(dayTag.getName());
      }

      RepositoryFactory.getRepository().getWorkItemRepository().setRegularTime(calendarDay, calendarDay,
          dayTag.getRegularTime(), currentTags);

    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    return null;
  }

}
