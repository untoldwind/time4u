package de.objectcode.time4u.client.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

import de.objectcode.time4u.server.api.data.Task;

public class TaskAdapterFactory implements IAdapterFactory
{
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Object adaptableObject, final Class adapterType)
  {
    if (!(adaptableObject instanceof Task)) {
      return null;
    }

    if (IActionFilter.class.isAssignableFrom(adapterType)) {
      return new TaskActionFilter();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Class[] getAdapterList()
  {
    return new Class[] { IActionFilter.class };
  }

  static class TaskActionFilter implements IActionFilter
  {
    public boolean testAttribute(final Object target, final String name, final String value)
    {
      final Task task = (Task) target;

      if ("active".equals(name)) {
        return Boolean.parseBoolean(value) == task.isActive();
      }

      return false;
    }

  }

}
