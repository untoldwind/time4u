package de.objectcode.time4u.client.ui.views;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.ui.ICommandIds;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.actions.ProjectActionDelegate;
import de.objectcode.time4u.client.ui.dialogs.ProjectCopyDialog;
import de.objectcode.time4u.client.ui.dialogs.ProjectMoveDialog;
import de.objectcode.time4u.client.ui.dnd.ProjectTransfer;
import de.objectcode.time4u.client.ui.provider.ProjectContentProvider;
import de.objectcode.time4u.client.ui.provider.ProjectLabelProvider;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.client.ui.util.CompoundSelectionProvider;
import de.objectcode.time4u.client.ui.util.SelectionServiceAdapter;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

public class ProjectTreeView extends ViewPart implements IRepositoryListener
{
  public static final String ID = "de.objectcode.time4u.client.ui.view.projectTree";
  public static final String CONTEXT_ID = "de.objectcode.time4u.ui.context.project";

  private TreeViewer m_viewer;
  private boolean m_showOnlyActive;

  int m_refreshCounter = 0;
  private CompoundSelectionProvider m_selectionProvider;
  private IContextActivation m_contextActivation;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    m_viewer.setContentProvider(new ProjectContentProvider(RepositoryFactory.getRepository().getProjectRepository(),
        m_showOnlyActive));
    m_viewer.setLabelProvider(new ProjectLabelProvider());
    m_viewer.setInput(new Object());

    m_selectionProvider = new CompoundSelectionProvider();
    m_selectionProvider.addPostSelectionProvider(CompoundSelectionEntityType.PROJECT, m_viewer);
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
          final IHandlerService handlerService = (IHandlerService) getSite().getWorkbenchWindow().getWorkbench()
              .getService(IHandlerService.class);

          handlerService.executeCommand(ICommandIds.CMD_PROJECT_EDIT, null);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    });
    m_viewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT, new Transfer[] {
      ProjectTransfer.getInstance()
    }, new DragSourceAdapter() {
      @Override
      public void dragSetData(final DragSourceEvent event)
      {
        final IStructuredSelection selection = (IStructuredSelection) m_viewer.getSelection();
        event.data = selection.getFirstElement();
      }
    });
    m_viewer.addDropSupport(DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT, new Transfer[] {
      ProjectTransfer.getInstance()
    }, new DropTargetAdapter() {
      @Override
      public void drop(final DropTargetEvent event)
      {
        if (event.data == null || !(event.data instanceof ProjectSummary)) {
          return;
        }
        ProjectSummary newParent = null;
        if (event.item != null && event.item.getData() != null && event.item.getData() instanceof ProjectSummary) {
          newParent = (ProjectSummary) event.item.getData();
        }
        try {
          final Project project = RepositoryFactory.getRepository().getProjectRepository().getProject(
              ((ProjectSummary) event.data).getId());

          if ((event.detail & DND.DROP_MOVE) != 0) {
            final ProjectMoveDialog dialog = new ProjectMoveDialog(getSite(), project, newParent);

            if (dialog.open() == ProjectMoveDialog.OK) {
              newParent = dialog.getNewParent();
              // Test for a look (i.e. dragging a parent to one of its own children)
              ProjectSummary current = newParent;
              while (current != null) {
                if (current.getId().equals(project.getId())) {
                  // We have loop
                  return;
                }
                if (current.getParentId() != null) {
                  current = RepositoryFactory.getRepository().getProjectRepository().getProjectSummary(
                      current.getParentId());
                } else {
                  current = null;
                }
              }

              project.setParentId(newParent != null ? newParent.getId() : null);

              RepositoryFactory.getRepository().getProjectRepository().storeProject(project, true);
            }
          } else if ((event.detail & DND.DROP_COPY) != 0) {
            final ProjectCopyDialog dialog = new ProjectCopyDialog(getSite(), project, newParent);

            if (dialog.open() == ProjectCopyDialog.OK) {
              ProjectActionDelegate.copyProject(project, dialog.getNewName(), dialog.getNewParent(), dialog
                  .isCopyTasks(), dialog.isCopySubProjects());
            }

          }
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }

      @Override
      public void dragOver(final DropTargetEvent event)
      {
        event.feedback = DND.FEEDBACK_NONE;

        if (event.item == null) {
          event.feedback = DND.FEEDBACK_SELECT;
        } else if (event.item instanceof TreeItem) {
          final Object data = ((TreeItem) event.item).getData();

          if (data != null && data instanceof ProjectSummary) {
            event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SELECT;
          }
        }
      }
    });

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.PROJECT, this);
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);

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

      m_viewer.setContentProvider(new ProjectContentProvider(RepositoryFactory.getRepository().getProjectRepository(),
          m_showOnlyActive));

      m_viewer.refresh();
      m_viewer.setSelection(selection);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    final IContextService contextService = (IContextService) getSite().getService(IContextService.class);
    contextService.deactivateContext(m_contextActivation);

    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.PROJECT, this);
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.ACTIVE_WORKITEM, this);

    super.dispose();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus()
  {
    m_viewer.getControl().setFocus();
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

  /**
   * {@inheritDoc}
   */
  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    switch (event.getEventType()) {
      case PROJECT:
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
              final Object[] expanded = m_viewer.getExpandedElements();

              m_viewer.setInput(new Object());

              m_viewer.setExpandedElements(expanded);
              m_viewer.setSelection(selection);
            } finally {
              synchronized (ProjectTreeView.this) {
                m_refreshCounter--;
              }
            }
          }
        });

        break;
      case ACTIVE_WORKITEM:
        try {
          final WorkItem activeWorkItem = RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem();

          if (activeWorkItem != null) {
            final ProjectSummary project = RepositoryFactory.getRepository().getProjectRepository().getProjectSummary(
                activeWorkItem.getProjectId());

            m_viewer.setSelection(new StructuredSelection(project));
          }
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }

        break;
      default:
        break;
    }
  }
}
