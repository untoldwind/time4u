package de.objectcode.time4u.client.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

import de.objectcode.time4u.server.api.data.ProjectSummary;

public class ProjectAdapterFactory implements IAdapterFactory
{

  @SuppressWarnings("unchecked")
  public Object getAdapter(final Object adaptableObject, final Class adapterType)
  {
    if (!(adaptableObject instanceof ProjectSummary)) {
      return null;
    }

    if (IActionFilter.class.isAssignableFrom(adapterType)) {
      return new ProjectActionFilter();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Class[] getAdapterList()
  {
    return new Class[] { IActionFilter.class };
  }

  static class ProjectActionFilter implements IActionFilter
  {
    public boolean testAttribute(final Object target, final String name, final String value)
    {
      final ProjectSummary project = (ProjectSummary) target;

      if ("active".equals(name)) {
        return Boolean.parseBoolean(value) == project.isActive();
      } else if ("deleted".equals(name)) {
        return Boolean.parseBoolean(value) == project.isDeleted();
      }

      return false;
    }

  }

}
