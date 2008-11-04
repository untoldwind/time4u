package de.objectcode.time4u.client.ui.views;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.dnd.TodoTransfer;
import de.objectcode.time4u.client.ui.provider.TodoContentProvider;
import de.objectcode.time4u.client.ui.provider.TodoLabelProvider;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.client.ui.util.CompoundSelectionProvider;
import de.objectcode.time4u.client.ui.util.SelectionServiceAdapter;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoTreeView extends ViewPart implements IRepositoryListener
{
  public static final String ID = "de.objectcode.time4u.client.ui.view.todoTree";

  private TreeViewer m_viewer;

  int m_refreshCounter = 0;
  private CompoundSelectionProvider m_selectionProvider;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    m_viewer.setContentProvider(new TodoContentProvider(RepositoryFactory.getRepository().getTodoRepository()));
    m_viewer.setLabelProvider(new TodoLabelProvider());
    m_viewer.setInput(new Object());
    m_viewer.addDragSupport(DND.DROP_MOVE | DND.DROP_DEFAULT, new Transfer[] { TodoTransfer.getInstance() },
        new DragSourceAdapter() {
          @Override
          public void dragSetData(final DragSourceEvent event)
          {
            final IStructuredSelection selection = (IStructuredSelection) m_viewer.getSelection();
            event.data = selection.getFirstElement();
          }
        });
    m_viewer.addDropSupport(DND.DROP_MOVE | DND.DROP_DEFAULT, new Transfer[] { TodoTransfer.getInstance() },
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

    m_selectionProvider = new CompoundSelectionProvider();
    m_selectionProvider.addPostSelectionProvider(CompoundSelectionEntityType.TODO, m_viewer);
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

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.TODO, this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.TODO, this);

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
              synchronized (TodoTreeView.this) {
                m_refreshCounter--;
              }
            }
          }
        });

        break;
    }
  }
}
