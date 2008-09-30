package de.objectcode.time4u.client.connection.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.objectcode.time4u.client.connection.api.ConnectionFactory;
import de.objectcode.time4u.client.connection.api.IConnection;
import de.objectcode.time4u.client.connection.ui.dialogs.ConnectionDialog;
import de.objectcode.time4u.client.store.api.RepositoryFactory;

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

        try {
          final IConnection connection = ConnectionFactory.openConnection(dialog.getServerConnection());

          System.out.println(">>> " + connection);
          if (!connection.testConnection()) {
            MessageDialog.openError(m_shellProvider.getShell(), "Connection error",
                "Server is incompatible with this client version");
            return;
          }
          System.out.println(">>> tested");
          if (!connection.checkLogin(dialog.getServerConnection().getCredentials())) {
            System.out.println(">>> check login failed");
            if (!connection.registerLogin(dialog.getServerConnection().getCredentials())) {
              System.out.println(">>> register login failed");
              MessageDialog.openError(m_shellProvider.getShell(), "Connection error", "Failed to register login");
            }
          }

          RepositoryFactory.getRepository().getServerConnectionRepository().storeServerConnection(
              dialog.getServerConnection());
        } catch (final Throwable e) {
          e.printStackTrace();
          MessageDialog.openError(m_shellProvider.getShell(), "Connection error", "Failed to contact server: "
              + e.getMessage());
        }
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
