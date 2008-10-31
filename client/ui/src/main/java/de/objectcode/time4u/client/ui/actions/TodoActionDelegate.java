package de.objectcode.time4u.client.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import de.objectcode.time4u.client.ui.dialogs.PersonListDialog;
import de.objectcode.time4u.client.ui.dialogs.TeamListDialog;
import de.objectcode.time4u.client.ui.views.TodoTreeView;

public class TodoActionDelegate implements IWorkbenchWindowActionDelegate
{
  IWorkbenchWindow m_window;
  IShellProvider m_shellProvider;

  public void init(final IWorkbenchWindow window)
  {
    m_window = window;
    m_shellProvider = new SameShellProvider(window.getShell());
  }

  public void run(final IAction action)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.ui.todo.person".equals(id)) {
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

  public void selectionChanged(final IAction action, final ISelection selection)
  {
  }

  public void dispose()
  {
  }
}
