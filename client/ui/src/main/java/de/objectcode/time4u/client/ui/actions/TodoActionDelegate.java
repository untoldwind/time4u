package de.objectcode.time4u.client.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.objectcode.time4u.client.ui.dialogs.PersonListDialog;

public class TodoActionDelegate implements IWorkbenchWindowActionDelegate
{
  IShellProvider m_shellProvider;

  public void init(final IWorkbenchWindow window)
  {
    m_shellProvider = new SameShellProvider(window.getShell());
  }

  public void run(final IAction action)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.ui.todo.person".equals(id)) {
      final PersonListDialog dialog = new PersonListDialog(m_shellProvider);

      dialog.open();
    }
  }

  public void selectionChanged(final IAction action, final ISelection selection)
  {
  }

  public void dispose()
  {
  }
}
