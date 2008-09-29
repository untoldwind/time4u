package de.objectcode.time4u.client.connection.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class ConnectionActionDelegate implements IWorkbenchWindowActionDelegate
{

  public void init(final IWorkbenchWindow window)
  {
    // TODO Auto-generated method stub

  }

  public void run(final IAction action)
  {
    System.out.println(">> " + action.getId());
    // TODO Auto-generated method stub

  }

  public void dispose()
  {
  }

  public void selectionChanged(final IAction action, final ISelection selection)
  {
  }

}
