package de.objectcode.time4u.client.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.util.CompoundSelection;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

public class CompoundSelectionAdapterFactory implements IAdapterFactory
{
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Object adaptableObject, final Class adapterType)
  {
    if (!(adaptableObject instanceof CompoundSelection)) {
      return null;
    }

    if (IActionFilter.class.isAssignableFrom(adapterType)) {
      return new MultiEntitySelectionActionFilter();
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public Class[] getAdapterList()
  {
    return new Class[] { IActionFilter.class, Project.class, ProjectSummary.class, Task.class, TaskSummary.class };
  }

  static class MultiEntitySelectionActionFilter implements IActionFilter
  {
    public boolean testAttribute(final Object target, final String name, final String value)
    {
      final CompoundSelection selection = (CompoundSelection) target;

      if ("has".equals(name)) {
        return selection.getSelection(CompoundSelectionEntityType.valueOf(value)) != null;
      } else if ("PROJECT.active".equals(name)) {
        final ProjectSummary project = (ProjectSummary) selection.getSelection(CompoundSelectionEntityType.PROJECT);

        if (project != null) {
          return Boolean.parseBoolean(value) == project.isActive();
        }
      } else if ("TASK.active".equals(name)) {
        final TaskSummary task = (TaskSummary) selection.getSelection(CompoundSelectionEntityType.TASK);

        if (task != null) {
          return Boolean.parseBoolean(value) == task.isActive();
        }
      } else if ("WORKITEM.active".equals(name)) {
        final WorkItem workItem = (WorkItem) selection.getSelection(CompoundSelectionEntityType.WORKITEM);

        if (workItem != null) {
          try {
            final WorkItem activeWorkItem = RepositoryFactory.getRepository().getWorkItemRepository()
                .getActiveWorkItem();

            return Boolean.parseBoolean(value) == (activeWorkItem != null && workItem.getId().equals(
                activeWorkItem.getId()));
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      } else if ("TODO.hasTask".equals(name)) {
        final TodoSummary todoSummary = (TodoSummary) selection.getSelection(CompoundSelectionEntityType.TODO);

        if (todoSummary != null) {
          try {
            final Todo todo = RepositoryFactory.getRepository().getTodoRepository().getTodo(todoSummary.getId());

            return Boolean.parseBoolean(value) == (todo != null && todo.getTaskId() != null);
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      } else if ("TODO.group".equals(name)) {
        final TodoSummary todo = (TodoSummary) selection.getSelection(CompoundSelectionEntityType.TODO);

        if (todo != null) {
          return Boolean.parseBoolean(value) == todo.isGroup();
        }
      } else if ("TODO.completed".equals(name)) {
        final TodoSummary todo = (TodoSummary) selection.getSelection(CompoundSelectionEntityType.TODO);

        if (todo != null) {
          return Boolean.parseBoolean(value) == todo.isCompleted();
        }
      }

      return false;
    }
  }
}
