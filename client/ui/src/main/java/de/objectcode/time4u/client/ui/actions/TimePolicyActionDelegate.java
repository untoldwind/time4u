package de.objectcode.time4u.client.ui.actions;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
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
import de.objectcode.time4u.client.ui.dialogs.TimePolicyDialog;
import de.objectcode.time4u.client.ui.dialogs.TimePolicyOverrideDialog;
import de.objectcode.time4u.server.api.data.CalendarDay;

public class TimePolicyActionDelegate implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
  IShellProvider m_shellProvider;
  IWorkbenchWindow m_window;
  IAdaptable m_selection;

  /**
   * {@inheritDoc}
   */
  public void init(final IWorkbenchWindow window)
  {
    m_shellProvider = new SameShellProvider(window.getShell());
    m_window = window;
  }

  /**
   * {@inheritDoc}
   */
  public void init(final IViewPart view)
  {
    m_shellProvider = view.getSite();
  }

  /**
   * {@inheritDoc}
   */
  public void run(final IAction action)
  {
    final String id = action.getId();

    if (m_selection == null) {
      m_selection = (IAdaptable) m_window.getSelectionService().getSelection();
    }
    final CalendarDay selectedDay = (CalendarDay) m_selection.getAdapter(CalendarDay.class);

    if ("de.objectcode.time4u.client.ui.timepolicy.manage".equals(id)) {
      final TimePolicyDialog dialog = new TimePolicyDialog(m_shellProvider, RepositoryFactory.getRepository()
          .getWorkItemRepository());

      dialog.open();
    } else if ("de.objectcode.time4u.client.ui.timepolicy.override".equals(id)) {
      if (selectedDay != null) {
        final TimePolicyOverrideDialog dialog = new TimePolicyOverrideDialog(m_shellProvider, RepositoryFactory
            .getRepository().getWorkItemRepository(), selectedDay);

        dialog.open();
      }
    } else if ("de.objectcode.time4u.client.ui.timepolicy.markregular".equals(id)) {
      if (selectedDay != null) {
        try {
          RepositoryFactory.getRepository().getWorkItemRepository().setRegularTime(selectedDay, selectedDay, -1, null);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.ui.timepolicy.markfree".equals(id)) {
      if (selectedDay != null) {
        try {
          RepositoryFactory.getRepository().getWorkItemRepository().setRegularTime(selectedDay, selectedDay, 0, null);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
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

  /**
   * {@inheritDoc}
   */
  public void dispose()
  {
  }
}
