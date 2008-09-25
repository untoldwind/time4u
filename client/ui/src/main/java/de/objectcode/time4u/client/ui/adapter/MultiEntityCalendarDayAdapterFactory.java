package de.objectcode.time4u.client.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;

import de.objectcode.time4u.client.ui.util.MultiEntitySelection;
import de.objectcode.time4u.client.ui.util.SelectionEntityType;
import de.objectcode.time4u.server.api.data.CalendarDay;

public class MultiEntityCalendarDayAdapterFactory implements IAdapterFactory
{
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Object adaptableObject, final Class adapterType)
  {
    if (!(adaptableObject instanceof MultiEntitySelection)) {
      return null;
    }

    final MultiEntitySelection selection = (MultiEntitySelection) adaptableObject;

    if (CalendarDay.class.isAssignableFrom(adapterType)) {
      final Object sel = selection.getSelection(SelectionEntityType.CALENDARDAY);

      if (sel instanceof CalendarDay) {
        return sel;
      }
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public Class[] getAdapterList()
  {
    return new Class[] { CalendarDay.class };
  }

}