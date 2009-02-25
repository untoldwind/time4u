package de.objectcode.time4u.client.ui.views;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.ui.ICommandIds;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.dnd.TaskTransfer;
import de.objectcode.time4u.client.ui.provider.TaskContentProvider;
import de.objectcode.time4u.client.ui.provider.TaskLabelProvider;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.client.ui.util.CompoundSelectionProvider;
import de.objectcode.time4u.client.ui.util.SelectionServiceAdapter;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;

public class TaskListView extends ViewPart implements IRepositoryListener, ISelectionListener
{
  public static final String ID = "de.objectcode.time4u.client.ui.view.taskList";
  public static final String CONTEXT_ID = "de.objectcode.time4u.ui.context.task";

  private ProjectSummary m_selectedProject;
  private TableViewer m_viewer;
  private ITaskRepository m_taskRepository;
  private boolean m_showOnlyActive;

  int m_refreshCounter = 0;

  private CompoundSelectionProvider m_selectionProvider;
  private IContextActivation m_contextActivation;

  /**
   * This is a callback that will allow us to create the viewer and initialize it.
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_taskRepository = RepositoryFactory.getRepository().getTaskRepository();

    m_viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    m_viewer.setContentProvider(new TaskContentProvider(m_taskRepository, m_showOnlyActive));
    m_viewer.setLabelProvider(new TaskLabelProvider());
    m_viewer.setInput(null);
    m_viewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT, new Transfer[] { TaskTransfer
        .getInstance() }, new DragSourceAdapter() {
      @Override
      public void dragSetData(final DragSourceEvent event)
      {
        final IStructuredSelection selection = (IStructuredSelection) m_viewer.getSelection();
        final TaskTransfer.ProjectTask projectTask = new TaskTransfer.ProjectTask(m_selectedProject,
            (TaskSummary) selection.getFirstElement());
        event.data = projectTask;
      }
    });

    m_selectionProvider = new CompoundSelectionProvider();
    m_selectionProvider.addPostSelectionProvider(CompoundSelectionEntityType.TASK, m_viewer);
    getSite().setSelectionProvider(m_selectionProvider);
    getSite().getPage().addSelectionListener(m_selectionProvider);

    final MenuManager menuMgr = new MenuManager();

    menuMgr.add(new GroupMarker("newGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker("objectGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker("deleteGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

    final Menu menu = menuMgr.createContextMenu(m_viewer.getControl());
    m_viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, new SelectionServiceAdapter(getSite().getPage()));

    m_viewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(final DoubleClickEvent event)
      {
        try {
          final ICommandService commandService = (ICommandService) getSite().getWorkbenchWindow().getWorkbench()
              .getService(ICommandService.class);
          final Command command = commandService.getCommand(ICommandIds.CMD_TASK_EDIT);

          command.executeWithChecks(new ExecutionEvent());
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    });

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.TASK, this);

    getSite().getPage().addSelectionListener(ProjectTreeView.ID, this);

    final IContextService contextService = (IContextService) getSite().getService(IContextService.class);
    m_contextActivation = contextService.activateContext(CONTEXT_ID);
  }

  public boolean isShowOnlyActive()
  {
    return m_showOnlyActive;
  }

  public void setShowOnlyActive(final boolean showOnlyActive)
  {
    if (m_showOnlyActive != showOnlyActive) {
      m_showOnlyActive = showOnlyActive;
      final ISelection selection = m_viewer.getSelection();
      m_viewer.setContentProvider(new TaskContentProvider(m_taskRepository, m_showOnlyActive));
      m_viewer.refresh();
      m_viewer.setSelection(selection);
    }
  }

  public Task getSelectedTask()
  {
    final ISelection selection = m_viewer.getSelection();

    if (selection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) selection).getFirstElement();

      if (obj != null && obj instanceof Task) {
        return (Task) obj;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    final IContextService contextService = (IContextService) getSite().getService(IContextService.class);
    contextService.deactivateContext(m_contextActivation);

    getSite().getPage().removeSelectionListener(ProjectTreeView.ID, this);

    super.dispose();
  }

  /**
   * {@inheritDoc}
   */
  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    switch (event.getEventType()) {
      case TASK:
        // Do not queue more than 2 refreshes
        synchronized (this) {
          if (m_refreshCounter >= 2) {
            return;
          }
          m_refreshCounter++;
        }
        m_viewer.getControl().getDisplay().asyncExec(new Runnable() {
          public void run()
          {
            try {
              final ISelection selection = m_viewer.getSelection();
              m_viewer.refresh();
              m_viewer.setSelection(selection);
            } finally {
              synchronized (TaskListView.this) {
                m_refreshCounter--;
              }
            }
          }
        });
        break;
    }
  }

  /*
   * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
   */
  @Override
  public void setFocus()
  {
    m_viewer.getControl().setFocus();
  }

  /**
   * {@inheritDoc}
   */
  public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
  {
    if (selection instanceof IAdaptable) {
      m_selectedProject = (ProjectSummary) ((IAdaptable) selection).getAdapter(ProjectSummary.class);
      m_viewer.setInput(m_selectedProject);
      return;
    } else if (selection instanceof IStructuredSelection) {
      final Object sel = ((IStructuredSelection) selection).getFirstElement();

      if (sel != null && sel instanceof Project) {
        m_selectedProject = (Project) sel;
        m_viewer.setInput(sel);
        return;
      }
    }

    m_viewer.setInput(null);
    m_selectedProject = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(final IViewSite site, final IMemento memento) throws PartInitException
  {
    super.init(site, memento);

    if (memento != null) {
      final Integer showOnlyActive = memento.getInteger("showOnlyActive");

      if (showOnlyActive != null) {
        m_showOnlyActive = showOnlyActive != 0;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveState(final IMemento memento)
  {
    super.saveState(memento);

    memento.putInteger("showOnlyActive", m_showOnlyActive ? 1 : 0);
  }
}
