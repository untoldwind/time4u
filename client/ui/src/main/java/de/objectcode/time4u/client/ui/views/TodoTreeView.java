package de.objectcode.time4u.client.ui.views;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.ui.ICommandIds;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.dnd.TodoTransfer;
import de.objectcode.time4u.client.ui.provider.TodoFilterSettings;
import de.objectcode.time4u.client.ui.provider.TodoLabelProvider;
import de.objectcode.time4u.client.ui.provider.TodoTableContentProvider;
import de.objectcode.time4u.client.ui.provider.TodoTreeContentProvider;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.client.ui.util.CompoundSelectionProvider;
import de.objectcode.time4u.client.ui.util.SelectionServiceAdapter;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoTreeView extends ViewPart implements IRepositoryListener
{
  public static final String ID = "de.objectcode.time4u.client.ui.view.todoTree";
  public static final String CONTEXT_ID = "de.objectcode.time4u.ui.context.todo";

  private TreeViewer m_treeViewer;
  private TableViewer m_flatViewer;
  private PageBook m_pageBook;

  int m_refreshCounter = 0;
  private CompoundSelectionProvider m_selectionProvider;

  private ViewType m_viewType = ViewType.TREE;
  private final TodoFilterSettings m_filterSettings = new TodoFilterSettings();
  private IContextActivation m_contextActivation;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_pageBook = new PageBook(parent, SWT.NONE);

    m_treeViewer = new TreeViewer(m_pageBook, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    m_treeViewer.setContentProvider(new TodoTreeContentProvider(RepositoryFactory.getRepository().getTodoRepository(),
        m_filterSettings));
    m_treeViewer.setLabelProvider(new TodoLabelProvider());
    m_treeViewer.setInput(new Object());
    m_treeViewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(final DoubleClickEvent event)
      {
        try {
          final IHandlerService handlerService = (IHandlerService) getSite().getWorkbenchWindow().getWorkbench()
              .getService(IHandlerService.class);

          handlerService.executeCommand(ICommandIds.CMD_TODO_EDIT, null);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    });
    m_treeViewer.addDragSupport(DND.DROP_MOVE | DND.DROP_DEFAULT, new Transfer[] { TodoTransfer.getInstance() },
        new DragSourceAdapter() {
          @Override
          public void dragSetData(final DragSourceEvent event)
          {
            final IStructuredSelection selection = (IStructuredSelection) m_treeViewer.getSelection();
            event.data = selection.getFirstElement();
          }
        });
    m_treeViewer.addDropSupport(DND.DROP_MOVE | DND.DROP_DEFAULT, new Transfer[] { TodoTransfer.getInstance() },
        new DropTargetAdapter() {
          @Override
          public void drop(final DropTargetEvent event)
          {
            if (event.data == null || !(event.data instanceof TodoSummary)) {
              return;
            }

            final TodoSummary todoSummary = (TodoSummary) event.data;
            try {
              if (todoSummary.isGroup()) {
                final TodoGroup todoGroup = RepositoryFactory.getRepository().getTodoRepository().getTodoGroup(
                    todoSummary.getId());
                if (event.item == null) {
                  todoGroup.setGroupdId(null);
                } else if (event.item instanceof TreeItem) {
                  final Object data = ((TreeItem) event.item).getData();

                  if (data != null && data instanceof TodoSummary) {
                    final TodoSummary target = (TodoSummary) data;

                    if (target.isGroup()) {
                      // Test for a look (i.e. dragging a parent to one of its own children)
                      TodoSummary current = target;
                      while (current != null) {
                        if (current.getId().equals(todoGroup.getId())) {
                          // We have loop
                          return;
                        }
                        if (current.getGroupdId() != null) {
                          current = RepositoryFactory.getRepository().getTodoRepository().getTodoSummary(
                              current.getGroupdId());
                        } else {
                          current = null;
                        }
                      }
                      todoGroup.setGroupdId(target.getId());
                    }
                  }
                }
                RepositoryFactory.getRepository().getTodoRepository().storeTodoGroup(todoGroup, true);
              } else {
                final Todo todo = RepositoryFactory.getRepository().getTodoRepository().getTodo(todoSummary.getId());
                if (event.item == null) {
                  todo.setGroupdId(null);
                } else if (event.item instanceof TreeItem) {
                  final Object data = ((TreeItem) event.item).getData();

                  if (data != null && data instanceof TodoSummary) {
                    final TodoSummary target = (TodoSummary) data;

                    if (target.isGroup()) {
                      todo.setGroupdId(target.getId());
                    }
                  }
                }
                RepositoryFactory.getRepository().getTodoRepository().storeTodo(todo, true);
              }
            } catch (final RepositoryException e) {
              UIPlugin.getDefault().log(e);
            }
          }

          @Override
          public void dragOver(final DropTargetEvent event)
          {
            event.detail = DND.DROP_NONE;
            event.feedback = DND.FEEDBACK_NONE;

            if (event.item == null) {
              event.detail = event.operations;
              event.feedback = DND.FEEDBACK_SELECT;
            } else if (event.item instanceof TreeItem) {
              final Object data = ((TreeItem) event.item).getData();

              if (data != null && data instanceof TodoSummary) {
                final TodoSummary todo = (TodoSummary) data;

                if (todo.isGroup()) {
                  event.detail = event.operations;
                  event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SELECT;
                }
              }
            }
          }
        });

    m_flatViewer = new TableViewer(m_pageBook, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    m_flatViewer.setContentProvider(new TodoTableContentProvider(RepositoryFactory.getRepository().getTodoRepository(),
        m_filterSettings));
    m_flatViewer.setLabelProvider(new TodoLabelProvider());
    m_flatViewer.setInput(new Object());
    m_flatViewer.getTable().setLinesVisible(true);
    m_flatViewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(final DoubleClickEvent event)
      {
        try {
          final IHandlerService handlerService = (IHandlerService) getSite().getWorkbenchWindow().getWorkbench()
              .getService(IHandlerService.class);

          handlerService.executeCommand(ICommandIds.CMD_TODO_EDIT, null);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    });

    m_selectionProvider = new CompoundSelectionProvider();
    m_selectionProvider.addPostSelectionProvider(CompoundSelectionEntityType.TODO, m_treeViewer);
    m_selectionProvider.addPostSelectionProvider(CompoundSelectionEntityType.TODO, m_flatViewer);
    getSite().setSelectionProvider(m_selectionProvider);
    getSite().getPage().addSelectionListener(m_selectionProvider);

    final MenuManager menuMgr = new MenuManager();

    menuMgr.add(new GroupMarker("operationGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker("newGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker("objectGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker("deleteGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

    final Menu menu = menuMgr.createContextMenu(m_treeViewer.getControl());

    m_treeViewer.getControl().setMenu(menu);
    m_flatViewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, new SelectionServiceAdapter(getSite().getPage()));

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.TODO, this);

    switch (m_viewType) {
      case FLAT:
        m_pageBook.showPage(m_flatViewer.getTable());
        break;
      case TREE:
        m_pageBook.showPage(m_treeViewer.getTree());
        break;
    }

    final IContextService contextService = (IContextService) getSite().getService(IContextService.class);
    m_contextActivation = contextService.activateContext(CONTEXT_ID);
  }

  public ViewType getViewType()
  {
    return m_viewType;
  }

  public void setViewType(final ViewType viewType)
  {
    m_viewType = viewType;

    switch (m_viewType) {
      case FLAT:
        m_pageBook.showPage(m_flatViewer.getTable());
        m_flatViewer.setInput(new Object());
        break;
      case TREE:
        m_pageBook.showPage(m_treeViewer.getTree());
        final Object[] expanded = m_treeViewer.getExpandedElements();
        m_treeViewer.setInput(new Object());
        m_treeViewer.setExpandedElements(expanded);
        break;
    }
  }

  public TodoFilterSettings getFilterSettings()
  {
    return m_filterSettings;
  }

  public void refresh()
  {
    switch (m_viewType) {
      case TREE: {
        final ISelection selection = m_treeViewer.getSelection();
        final Object[] expanded = m_treeViewer.getExpandedElements();

        m_treeViewer.setInput(new Object());

        m_treeViewer.setExpandedElements(expanded);
        m_treeViewer.setSelection(selection);
        break;
      }
      case FLAT: {
        final ISelection selection = m_flatViewer.getSelection();

        m_flatViewer.setInput(new Object());

        m_flatViewer.setSelection(selection);
        break;
      }
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

    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.TODO, this);

    super.dispose();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus()
  {
    m_treeViewer.getControl().setFocus();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(final IViewSite site, final IMemento memento) throws PartInitException
  {
    super.init(site, memento);

    if (memento != null) {
      final String viewType = memento.getString("viewType");

      if (viewType != null) {
        m_viewType = ViewType.valueOf(viewType);
      }

      m_filterSettings.setOnlyVisible(memento.getBoolean("filter.onlyVisible") != null ? memento
          .getBoolean("filter.onlyVisible") : true);
      m_filterSettings.setUnassigned(memento.getBoolean("filter.unassigned") != null ? memento
          .getBoolean("filter.unassigned") : true);
      m_filterSettings.setAssignedToMe(memento.getBoolean("filter.assignedToMe") != null ? memento
          .getBoolean("filter.assignedToMe") : true);
      m_filterSettings.setAssignedToOther(memento.getBoolean("filter.assignedToOther") != null ? memento
          .getBoolean("filter.assignedToOther") : true);
      for (final TodoState state : TodoState.values()) {
        final Boolean value = memento.getBoolean("filter.state." + state);

        if (value == null || value) {
          m_filterSettings.getStates().add(state);
        } else {
          m_filterSettings.getStates().remove(state);
        }
      }
      final Boolean hideCreatedOlderThan = memento.getBoolean("filter.hideCreatedOlderThan");
      if (hideCreatedOlderThan != null && hideCreatedOlderThan) {
        m_filterSettings.setHideCreatedOlderThan(memento.getInteger("filter.hideCreatedOlderThan.value"));
      } else {
        m_filterSettings.setHideCreatedOlderThan(null);
      }
      final Boolean hideCompletedOlderThan = memento.getBoolean("filter.hideCompletedOlderThan");
      if (hideCompletedOlderThan != null && hideCompletedOlderThan) {
        m_filterSettings.setHideCompletedOlderThan(memento.getInteger("filter.hideCompletedOlderThan.value"));
      } else {
        m_filterSettings.setHideCompletedOlderThan(null);
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

    memento.putString("viewType", m_viewType.toString());
    memento.putBoolean("filter.onlyVisible", m_filterSettings.isOnlyVisible());
    memento.putBoolean("filter.unassigned", m_filterSettings.isUnassigned());
    memento.putBoolean("filter.assignedToMe", m_filterSettings.isAssignedToMe());
    memento.putBoolean("filter.assignedToOther", m_filterSettings.isAssignedToOther());
    for (final TodoState state : TodoState.values()) {
      memento.putBoolean("filter.state." + state, m_filterSettings.getStates().contains(state));
    }
    memento.putBoolean("filter.hideCreatedOlderThan", m_filterSettings.getHideCreatedOlderThan() != null);
    if (m_filterSettings.getHideCreatedOlderThan() != null) {
      memento.putInteger("filter.hideCreatedOlderThan.value", m_filterSettings.getHideCreatedOlderThan());
    }
    memento.putBoolean("filter.hideCompletedOlderThan", m_filterSettings.getHideCompletedOlderThan() != null);
    if (m_filterSettings.getHideCompletedOlderThan() != null) {
      memento.putInteger("filter.hideCompletedOlderThan.value", m_filterSettings.getHideCompletedOlderThan());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    switch (event.getEventType()) {
      case TODO:
        // Do not queue more than 2 refreshes
        synchronized (this) {
          if (m_refreshCounter >= 2) {
            return;
          }

          m_refreshCounter++;
        }

        switch (m_viewType) {
          case TREE:
            m_treeViewer.getControl().getDisplay().asyncExec(new Runnable() {
              public void run()
              {
                try {
                  final ISelection selection = m_treeViewer.getSelection();
                  final Object[] expanded = m_treeViewer.getExpandedElements();

                  m_treeViewer.setInput(new Object());

                  m_treeViewer.setExpandedElements(expanded);
                  m_treeViewer.setSelection(selection);
                } finally {
                  synchronized (TodoTreeView.this) {
                    m_refreshCounter--;
                  }
                }
              }
            });
            break;
          case FLAT:
            m_flatViewer.getControl().getDisplay().asyncExec(new Runnable() {
              public void run()
              {
                try {
                  final ISelection selection = m_flatViewer.getSelection();

                  m_flatViewer.setInput(new Object());

                  m_flatViewer.setSelection(selection);
                } finally {
                  synchronized (TodoTreeView.this) {
                    m_refreshCounter--;
                  }
                }
              }
            });
            break;
        }

        break;
    }
  }

  public enum ViewType
  {
    TREE,
    FLAT;
  }
}
