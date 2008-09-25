package de.objectcode.time4u.client.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.Project;
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
    } else if (Project.class.isAssignableFrom(adapterType)) {
      if (adaptableObject instanceof Project) {
        return adaptableObject;
      } else {
        try {
          return RepositoryFactory.getRepository().getProjectRepository().getProject(
              ((ProjectSummary) adaptableObject).getId());
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
