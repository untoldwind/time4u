package de.objectcode.time4u.client.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.WorkbenchException;

import de.objectcode.time4u.client.ui.CompactPerspective;
import de.objectcode.time4u.client.ui.Perspective;
import de.objectcode.time4u.client.ui.UIPlugin;

public class PerspectivesActionDelegate implements IWorkbenchWindowActionDelegate
{
  IWorkbenchWindow m_window;

  /**
   * {@inheritDoc}
   */
  public void init(final IWorkbenchWindow window)
  {
    m_window = window;
  }

  public void run(final IAction action)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.ui.perspective.full".equals(id)) {
      try {
        m_window.getWorkbench().showPerspective(Perspective.ID, m_window);
      } catch (final WorkbenchException e) {
        UIPlugin.getDefault().log(e);
      }
    } else if ("de.objectcode.time4u.client.ui.perspective.compact".equals(id)) {
      try {
        m_window.getWorkbench().showPerspective(CompactPerspective.ID, m_window);
      } catch (final WorkbenchException e) {
        UIPlugin.getDefault().log(e);
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
