package de.objectcode.time4u.client.connection.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.objectcode.time4u.client.connection.ui.dialogs.ConnectionDialog;

public class ConnectionActionDelegate implements IWorkbenchWindowActionDelegate
{
  IShellProvider m_shellProvider;

  public void init(final IWorkbenchWindow window)
  {
    m_shellProvider = new SameShellProvider(window.getShell());

  }

  public void run(final IAction action)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.connection.new".equals(id)) {
      final ConnectionDialog dialog = new ConnectionDialog(m_shellProvider);

      if (dialog.open() == ConnectionDialog.OK) {
        System.out.println(">>>" + dialog.getServerConnection().getUrl());
      }
    }
  }

  public void dispose()
  {
  }

  public void selectionChanged(final IAction action, final ISelection selection)
  {
  }

}
