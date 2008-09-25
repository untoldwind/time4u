package de.objectcode.time4u.client.ui.provider;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.dnd.ProjectTaskHolder;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.filter.TaskFilter;

public class TaskContentProvider implements IStructuredContentProvider
{
  private final ITaskRepository m_taskRepository;
  private final boolean m_onlyActive;

  public TaskContentProvider(final ITaskRepository taskRepository, final boolean onlyActive)
  {
    m_taskRepository = taskRepository;
    m_onlyActive = onlyActive;
  }

  /*
   * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
   */
  public Object[] getElements(final Object inputElement)
  {
    try {
      if (inputElement instanceof ProjectTaskHolder) {
        final TaskFilter filter = new TaskFilter();
        filter.setDeleted(false);
        filter.setProject(((ProjectTaskHolder) inputElement).getProject().getId());
        if (m_onlyActive) {
          filter.setActive(true);
        }
        final Collection<Task> tasks = m_taskRepository.getTasks(filter);

        if (tasks != null) {
          return tasks.toArray();
        }
      } else if (inputElement instanceof Project) {
        final TaskFilter filter = new TaskFilter();
        filter.setDeleted(false);
        filter.setProject(((Project) inputElement).getId());
        if (m_onlyActive) {
          filter.setActive(true);
        }
        final Collection<Task> tasks = m_taskRepository.getTasks(filter);

        if (tasks != null) {
          return tasks.toArray();
        }
      } else if (inputElement instanceof Task) {
        final TaskFilter filter = new TaskFilter();
        filter.setDeleted(false);
        filter.setProject(((Task) inputElement).getProjectId());
        if (m_onlyActive) {
          filter.setActive(true);
        }
        final Collection<Task> tasks = m_taskRepository.getTasks(filter);

        if (tasks != null) {
          return tasks.toArray();
        }
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    return new Object[0];
  }

  /*
   * @see org.eclipse.jface.viewers.IContentProvider#dispose()
   */
  public void dispose()
  {
  }

  /*
   * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
   * java.lang.Object)
   */
  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
  {
  }
}
