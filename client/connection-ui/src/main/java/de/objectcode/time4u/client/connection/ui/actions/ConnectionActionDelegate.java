package de.objectcode.time4u.client.connection.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import de.objectcode.time4u.client.connection.ui.dialogs.ManageConnectionsDialog;
import de.objectcode.time4u.client.connection.ui.views.SynchronizeView;
import de.objectcode.time4u.client.connection.ui.wizards.NewConnectionWizard;

public class ConnectionActionDelegate implements IWorkbenchWindowActionDelegate
{
  IWorkbenchWindow m_window;
  IShellProvider m_shellProvider;

  public void init(final IWorkbenchWindow window)
  {
    m_shellProvider = new SameShellProvider(window.getShell());
    m_window = window;
  }

  public void run(final IAction action)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.connection.new".equals(id)) {
      final WizardDialog wizardDialog = new WizardDialog(m_shellProvider.getShell(), new NewConnectionWizard());

      wizardDialog.open();
    } else if ("de.objectcode.time4u.client.connection.manage".equals(id)) {
      final ManageConnectionsDialog dialog = new ManageConnectionsDialog(m_shellProvider);

      dialog.open();
    } else if ("de.objectcode.time4u.client.connection.openView".equals(id)) {
      try {
        m_window.getActivePage().showView(SynchronizeView.ID, null, IWorkbenchPage.VIEW_ACTIVATE);
      } catch (final PartInitException e) {
        MessageDialog.openError(m_shellProvider.getShell(), "Error", "Error opening view:" + e.getMessage());
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
