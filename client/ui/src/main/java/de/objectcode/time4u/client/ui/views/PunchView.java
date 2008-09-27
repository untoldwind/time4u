package de.objectcode.time4u.client.ui.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.ActiveWorkItemRepositoryEvent;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.actions.PunchInAction;
import de.objectcode.time4u.client.ui.actions.PunchOutAction;
import de.objectcode.time4u.client.ui.preferences.PreferenceConstants;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

public class PunchView extends ViewPart implements ISelectionListener, IRepositoryListener
{
  public static final String ID = "de.objectcode.time4u.client.ui.view.punchView";

  private ProjectSummary m_selectedProject;
  private TaskSummary m_selectedTask;
  private ProjectSummary m_activeProject;
  private TaskSummary m_activeTask;
  private WorkItem m_activeWorkItem;

  Image m_punchedOutImage;
  Image m_punchedInImage;
  Button m_punchButton;
  Label m_projectLabel;
  Label m_taskLabel;

  @Override
  public void createPartControl(final Composite parent)
  {
    final Composite top = new Composite(parent, SWT.NONE);
    final GridLayout layout = new GridLayout();

    layout.marginHeight = 5;
    layout.marginWidth = 10;
    layout.numColumns = 2;

    top.setLayout(layout);

    final Label projectLabel = new Label(top, SWT.LEFT);

    projectLabel.setText(UIPlugin.getDefault().getString("project.label"));
    m_projectLabel = new Label(top, SWT.LEFT | SWT.BORDER);
    m_projectLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    final Label taskLabel = new Label(top, SWT.LEFT);

    taskLabel.setText(UIPlugin.getDefault().getString("task.label"));
    m_taskLabel = new Label(top, SWT.LEFT | SWT.BORDER);
    m_taskLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_punchButton = new Button(top, SWT.TOGGLE);

    final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();

    if (store.getBoolean(PreferenceConstants.UI_STOPWATCH_PUNCH)) {
      m_punchedInImage = UIPlugin.getImageDescriptor("/icons/PunchedOutBig.gif").createImage();
      m_punchedOutImage = UIPlugin.getImageDescriptor("/icons/PunchedInBig.gif").createImage();
    } else {
      m_punchedInImage = UIPlugin.getImageDescriptor("/icons/PunchedInBig.gif").createImage();
      m_punchedOutImage = UIPlugin.getImageDescriptor("/icons/PunchedOutBig.gif").createImage();
    }

    m_punchButton.setImage(m_punchedOutImage);

    final GridData data = new GridData(GridData.FILL_BOTH);

    data.horizontalSpan = 2;
    m_punchButton.setLayoutData(data);
    m_punchButton.setEnabled(false);
    m_punchButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        punch();
      }
    });

    try {
      m_activeWorkItem = RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem();

      if (m_activeWorkItem != null) {
        m_punchButton.setSelection(true);
        m_punchButton.setImage(m_punchedInImage);
        m_punchButton.setEnabled(true);
        m_activeTask = RepositoryFactory.getRepository().getTaskRepository().getTaskSummary(
            m_activeWorkItem.getTaskId());

        m_taskLabel.setText(m_activeTask != null ? m_activeTask.getName() : "");

        m_activeProject = RepositoryFactory.getRepository().getProjectRepository().getProjectSummary(
            m_activeWorkItem.getProjectId());

        m_projectLabel.setText(m_activeProject != null ? m_activeProject.getName() : "");
      }
    } catch (final Exception e) {
    }

    getSite().getPage().addSelectionListener(this);

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);

    final MenuManager menuMgr = new MenuManager();

    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(final IMenuManager manager)
      {
        manager.add(new PunchOutAction());
        manager.add(new Separator());
        manager.add(new GroupMarker("taskhistory"));

        for (final PunchInAction action : UIPlugin.getDefault().getTaskHistory()) {
          if (action.isEnabled()) {
            manager.appendToGroup("taskhistory", action);
          }
        }
        manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
      }
    });

    final Menu menu = menuMgr.createContextMenu(top);

    top.setMenu(menu);

    m_punchButton.setMenu(menu);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus()
  {
    m_punchButton.setFocus();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    getSite().getPage().removeSelectionListener(this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);

    super.dispose();

    m_punchedInImage.dispose();
    m_punchedOutImage.dispose();
  }

  /**
   * {@inheritDoc}
   */
  public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
  {
    if (selection instanceof IAdaptable) {
      m_selectedProject = (ProjectSummary) ((IAdaptable) selection).getAdapter(ProjectSummary.class);
      m_selectedTask = (TaskSummary) ((IAdaptable) selection).getAdapter(TaskSummary.class);

      if (m_activeProject != null) {
        m_projectLabel.setText(m_activeProject.getName());
      } else {
        m_projectLabel.setText(m_selectedProject != null ? m_selectedProject.getName() : "");
      }
      if (m_activeTask != null) {
        m_taskLabel.setText(m_activeTask.getName());
      } else {
        m_taskLabel.setText(m_selectedTask != null ? m_selectedTask.getName() : "");
      }
    }

    if (m_activeWorkItem != null) {
      m_punchButton.setEnabled(true);
    } else {
      m_punchButton.setEnabled(m_selectedTask != null && m_selectedTask.isActive() && m_selectedProject != null
          && m_selectedProject.isActive());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    switch (event.getEventType()) {
      case ACTIVE_WORKITEM:
        m_activeWorkItem = ((ActiveWorkItemRepositoryEvent) event).getActiveWorkItem();

        try {
          if (m_activeWorkItem != null) {
            m_activeTask = RepositoryFactory.getRepository().getTaskRepository().getTaskSummary(
                m_activeWorkItem.getTaskId());
            m_activeProject = RepositoryFactory.getRepository().getProjectRepository().getProjectSummary(
                m_activeWorkItem.getProjectId());
          }
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }

        m_punchButton.getDisplay().asyncExec(new Runnable() {
          public void run()
          {
            updateControls();
          }
        });

        break;
    }
  }

  private void punch()
  {
    if (m_activeWorkItem == null) {
      if (m_selectedTask == null || m_selectedProject == null) {
        m_punchButton.setSelection(false);
        m_punchButton.setImage(m_punchedOutImage);
        m_punchButton.setEnabled(m_selectedTask != null && m_selectedTask.isActive() && m_selectedProject != null
            && m_selectedProject.isActive());
      } else {
        final WorkItem workItem = UIPlugin.getDefault().punchIn(m_selectedProject, m_selectedTask);

        m_activeWorkItem = workItem;
        m_activeTask = m_selectedTask;
        m_activeProject = m_selectedProject;
        m_punchButton.setSelection(true);
        m_punchButton.setImage(m_punchedInImage);
        m_punchButton.setEnabled(true);
      }
    } else {
      UIPlugin.getDefault().punchOut();

      m_punchButton.setSelection(false);
      m_punchButton.setImage(m_punchedOutImage);
      m_punchButton.setEnabled(m_selectedTask != null && m_selectedTask.isActive() && m_selectedProject != null
          && m_selectedProject.isActive());
      m_activeWorkItem = null;
      m_activeTask = null;
      m_activeProject = null;
    }
    updateControls();
  }

  private void updateControls()
  {
    if (m_activeWorkItem == null) {
      m_activeTask = null;
      m_activeProject = null;

      m_punchButton.setSelection(false);
      m_punchButton.setImage(m_punchedOutImage);
      m_punchButton.setEnabled(m_selectedTask != null && m_selectedTask.isActive() && m_selectedProject != null
          && m_selectedProject.isActive());
    } else {
      m_punchButton.setSelection(true);
      m_punchButton.setImage(m_punchedInImage);
      m_punchButton.setEnabled(true);
    }

    if (m_activeTask != null) {
      m_taskLabel.setText(m_activeTask.getName());
    } else {
      m_taskLabel.setText(m_selectedTask != null ? m_selectedTask.getName() : "");
    }

    if (m_activeProject != null) {
      m_projectLabel.setText(m_activeProject.getName());
    } else {
      m_projectLabel.setText(m_selectedProject != null ? m_selectedProject.getName() : "");
    }
  }
}
