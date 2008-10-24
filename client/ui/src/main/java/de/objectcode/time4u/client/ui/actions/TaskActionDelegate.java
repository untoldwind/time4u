package de.objectcode.time4u.client.ui.actions;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.dialogs.TaskDeleteDialog;
import de.objectcode.time4u.client.ui.dialogs.TaskDialog;
import de.objectcode.time4u.client.ui.preferences.PreferenceConstants;
import de.objectcode.time4u.client.ui.views.TaskListView;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.Task;

public class TaskActionDelegate implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
  IShellProvider m_shellProvider;
  TaskListView m_view = null;

  IAdaptable m_selection;

  /**
   * {@inheritDoc}
   */
  public void init(final IWorkbenchWindow window)
  {
    m_shellProvider = new SameShellProvider(window.getShell());
    m_view = (TaskListView) window.getActivePage().findView(TaskListView.ID);
  }

  /**
   * {@inheritDoc}
   */
  public void init(final IViewPart view)
  {
    m_shellProvider = view.getSite();

    if (view instanceof TaskListView) {
      m_view = (TaskListView) view;
    }
  }

  /**
   * {@inheritDoc}
   */
  public void dispose()
  {
  }

  /**
   * {@inheritDoc}
   */
  public void run(final IAction action)
  {
    final String id = action.getId();

    if (m_selection == null) {
      m_selection = (IAdaptable) m_view.getSite().getSelectionProvider().getSelection();
    }

    final Project selectedProject = (Project) m_selection.getAdapter(Project.class);
    final Task selectedTask = (Task) m_selection.getAdapter(Task.class);

    if ("de.objectcode.time4u.client.ui.task.new".equals(id)) {
      if (selectedProject != null) {
        final TaskDialog dialog = new TaskDialog(m_shellProvider, selectedProject);

        dialog.getTask().setProjectId(selectedProject.getId());

        if (dialog.open() == TaskDialog.OK) {
          try {
            RepositoryFactory.getRepository().getTaskRepository().storeTask(dialog.getTask(), true);
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      }
    } else if ("de.objectcode.time4u.client.ui.task.edit".equals(id)) {
      if (selectedProject != null && selectedTask != null) {
        final TaskDialog dialog = new TaskDialog(m_shellProvider, selectedProject, selectedTask);

        if (dialog.open() == TaskDialog.OK) {
          try {
            RepositoryFactory.getRepository().getTaskRepository().storeTask(dialog.getTask(), true);
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      }
    } else if ("de.objectcode.time4u.client.ui.task.delete".equals(id)) {
      if (selectedTask != null) {
        final TaskDeleteDialog warningDialog = new TaskDeleteDialog(m_shellProvider, selectedTask);

        if (warningDialog.isWarningNecessary()) {
          if (warningDialog.open() != TaskDeleteDialog.OK) {
            return;
          }
        } else {
          final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();

          if (store.getBoolean(PreferenceConstants.UI_CONFIRM_TASK_DELETE)) {
            if (!MessageDialog.openQuestion(m_shellProvider.getShell(), "Task delete", "Delete Task '"
                + selectedTask.getName() + "'")) {
              return;
            }
          }
        }

        try {
          RepositoryFactory.getRepository().getTaskRepository().deleteTask(selectedTask);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.task.ui.onlyActive".equals(id)) {
      if (m_view != null) {
        m_view.setShowOnlyActive(action.isChecked());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void selectionChanged(final IAction action, final ISelection selection)
  {
    if (selection instanceof IAdaptable) {
      m_selection = (IAdaptable) selection;
    } else if (selection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) selection).getFirstElement();

      if (obj != null && obj instanceof IAdaptable) {
        m_selection = (IAdaptable) obj;
      }
    }

    if ("de.objectcode.time4u.client.task.ui.onlyActive".equals(action.getId())) {
      if (m_view != null) {
        action.setChecked(m_view.isShowOnlyActive());
      }
    }
  }
}
