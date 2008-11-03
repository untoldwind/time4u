package de.objectcode.time4u.client.ui.actions;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.dialogs.PersonListDialog;
import de.objectcode.time4u.client.ui.dialogs.TeamListDialog;
import de.objectcode.time4u.client.ui.dialogs.TodoDialog;
import de.objectcode.time4u.client.ui.dialogs.TodoGroupDialog;
import de.objectcode.time4u.client.ui.views.TodoTreeView;
import de.objectcode.time4u.server.api.data.TaskSummary;

public class TodoActionDelegate implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
  IWorkbenchWindow m_window;
  IShellProvider m_shellProvider;

  IAdaptable m_selection;

  /**
   * {@inheritDoc}
   */
  public void init(final IViewPart view)
  {
    m_shellProvider = view.getSite();
    m_window = view.getSite().getWorkbenchWindow();
  }

  /**
   * {@inheritDoc}
   */
  public void init(final IWorkbenchWindow window)
  {
    m_window = window;
    m_shellProvider = new SameShellProvider(window.getShell());
  }

  /**
   * {@inheritDoc}
   */
  public void run(final IAction action)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.ui.todo.new".equals(id)) {
      final TaskSummary task = (TaskSummary) m_selection.getAdapter(TaskSummary.class);
      final TodoDialog dialog = new TodoDialog(m_shellProvider, task);

      if (dialog.open() == TodoDialog.OK) {
        try {
          RepositoryFactory.getRepository().getTodoRepository().storeTodo(dialog.getTodo(), true);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todoGroup.new".equals(id)) {
      final TodoGroupDialog dialog = new TodoGroupDialog(m_shellProvider);

      if (dialog.open() == TodoGroupDialog.OK) {
        try {
          RepositoryFactory.getRepository().getTodoRepository().storeTodoGroup(dialog.getTodoGroup(), true);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todo.person".equals(id)) {
      final PersonListDialog dialog = new PersonListDialog(m_shellProvider);

      dialog.open();
    } else if ("de.objectcode.time4u.client.ui.todo.team".equals(id)) {
      final TeamListDialog dialog = new TeamListDialog(m_shellProvider);

      dialog.open();
    } else if ("de.objectcode.time4u.client.ui.todo.showView".equals(id)) {
      try {
        m_window.getActivePage().showView(TodoTreeView.ID, null, IWorkbenchPage.VIEW_ACTIVATE);
      } catch (final PartInitException e) {
        MessageDialog.openError(m_shellProvider.getShell(), "Error", "Error opening view:" + e.getMessage());
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
  }

  public void dispose()
  {
  }
}
