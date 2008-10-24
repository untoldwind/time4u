package de.objectcode.time4u.client.ui;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.ActiveWorkItemRepositoryEvent;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.ui.actions.PunchInAction;
import de.objectcode.time4u.client.ui.actions.PunchOutAction;
import de.objectcode.time4u.client.ui.preferences.PreferenceConstants;
import de.objectcode.time4u.client.ui.util.TimeFormat;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

/**
 * Helper class to create the tray icon and its context menu.
 * 
 * @author junglas
 */
public class ApplicationTray implements IRepositoryListener
{
  private final Image m_punchedInImage;
  private final Image m_punchedOutImage;
  private final TrayItem m_trayItem;
  private final Shell m_shell;
  private final MenuManager m_menuManager;

  public ApplicationTray(final Display disploy)
  {
    m_shell = new Shell(disploy, SWT.NONE);

    final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();

    if (store.getBoolean(PreferenceConstants.UI_STOPWATCH_PUNCH)) {
      m_punchedInImage = UIPlugin.getImageDescriptor("/icons/PunchedOut.gif").createImage();
      m_punchedOutImage = UIPlugin.getImageDescriptor("/icons/PunchedIn.gif").createImage();
    } else {
      m_punchedInImage = UIPlugin.getImageDescriptor("/icons/PunchedIn.gif").createImage();
      m_punchedOutImage = UIPlugin.getImageDescriptor("/icons/PunchedOut.gif").createImage();
    }
    final Tray tray = disploy.getSystemTray();

    // Some systems might not have a system tray
    if (tray == null) {
      m_trayItem = null;
      m_menuManager = null;
      return;
    }

    m_trayItem = new TrayItem(tray, SWT.NONE);
    m_trayItem.setToolTipText("Punched Out");
    m_trayItem.setImage(m_punchedOutImage);
    m_trayItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        if (window == null) {
          try {
            window = PlatformUI.getWorkbench().openWorkbenchWindow(null);
            window.getShell().setActive();
            window.getShell().forceActive();
          } catch (final Exception ex) {
            UIPlugin.getDefault().log(ex);
          }
        } else {
          if (window.getShell().getMinimized()) {
            window.getShell().setMinimized(false);
          }

          if (!window.getShell().isVisible()) {
            window.getShell().setVisible(true);
          }

          window.getShell().setActive();
          window.getShell().forceActive();
        }
      }
    });
    m_menuManager = new MenuManager();
    final Menu menu = m_menuManager.createContextMenu(m_shell);
    m_menuManager.setRemoveAllWhenShown(true);
    m_menuManager.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(final IMenuManager manager)
      {
        manager.add(new PunchOutAction());
        manager.add(new Separator());
        manager.add(new GroupMarker("taskhistory"));

        for (final PunchInAction action : UIPlugin.getDefault().getTaskHistory()) {
          manager.appendToGroup("taskhistory", action);
        }
      }
    });
    m_trayItem.addMenuDetectListener(new MenuDetectListener() {
      public void menuDetected(final MenuDetectEvent e)
      {
        menu.setVisible(true);
      }
    });

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.WORKITEM, this);

    try {
      final WorkItem activeWorkItem = RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem();

      updateTrayItem(activeWorkItem);
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
  }

  void dispose()
  {
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.WORKITEM, this);

    m_punchedInImage.dispose();
    m_punchedOutImage.dispose();

    if (m_trayItem != null) {
      m_trayItem.dispose();
    }

    if (m_shell != null) {
      m_shell.dispose();
    }
  }

  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    if (m_trayItem == null) {
      return;
    }

    switch (event.getEventType()) {
      case WORKITEM:
        try {
          final WorkItem activeWorkItem = RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem();

          if (activeWorkItem != null) {
            m_trayItem.getDisplay().asyncExec(new Runnable() {
              public void run()
              {
                updateTrayItem(activeWorkItem);
              }
            });
          }
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
        break;
      case ACTIVE_WORKITEM:
        m_trayItem.getDisplay().asyncExec(new Runnable() {
          public void run()
          {
            updateTrayItem(((ActiveWorkItemRepositoryEvent) event).getActiveWorkItem());
          }
        });
        break;
    }
  }

  private void updateTrayItem(final WorkItem activeWorkItem)
  {
    if (activeWorkItem != null) {
      String text = "";

      try {
        final ProjectSummary project = RepositoryFactory.getRepository().getProjectRepository().getProjectSummary(
            activeWorkItem.getProjectId());
        final TaskSummary task = RepositoryFactory.getRepository().getTaskRepository().getTaskSummary(
            activeWorkItem.getTaskId());

        text = project.getName() + " -> " + task.getName() + " - ";
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }
      m_trayItem.setImage(m_punchedInImage);
      m_trayItem.setToolTipText("PunchedIn - " + text + TimeFormat.format(activeWorkItem.getDuration()));
    } else {
      m_trayItem.setImage(m_punchedOutImage);
      m_trayItem.setToolTipText("PunchedOut");
    }
  }
}
