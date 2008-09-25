package de.objectcode.time4u.client.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;

public class TaskAdapterFactory implements IAdapterFactory
{
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Object adaptableObject, final Class adapterType)
  {
    if (!(adaptableObject instanceof TaskSummary)) {
      return null;
    }

    if (IActionFilter.class.isAssignableFrom(adapterType)) {
      return new TaskActionFilter();
    } else if (Task.class.isAssignableFrom(adapterType)) {
      if (adaptableObject instanceof Task) {
        return adaptableObject;
      } else {
        try {
          return RepositoryFactory.getRepository().getTaskRepository().getTask(((TaskSummary) adaptableObject).getId());
        } catch (final RepositoryException e) {
          UIPlugin.getDefault().log(e);
        }
      }
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
      final TaskSummary task = (TaskSummary) target;

      if ("active".equals(name)) {
        return Boolean.parseBoolean(value) == task.isActive();
      } else if ("deleted".equals(name)) {
        return Boolean.parseBoolean(value) == task.isDeleted();
      }

      return false;
    }
  }

}
