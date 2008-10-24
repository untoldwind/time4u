package de.objectcode.time4u.client.ui.views;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.ui.ICommandIds;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.provider.ProjectContentProvider;
import de.objectcode.time4u.client.ui.provider.ProjectLabelProvider;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.client.ui.util.CompoundSelectionProvider;
import de.objectcode.time4u.client.ui.util.SelectionServiceAdapter;

public class ProjectTreeView extends ViewPart implements IRepositoryListener
{
  public static final String ID = "de.objectcode.time4u.client.ui.view.projectTree";

  private TreeViewer m_viewer;
  private boolean m_showOnlyActive;

  int m_refreshCounter = 0;
  private CompoundSelectionProvider m_selectionProvider;

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
          final ICommandService commandService = (ICommandService) getSite().getWorkbenchWindow().getWorkbench()
              .getService(ICommandService.class);

          final Command command = commandService.getCommand(ICommandIds.CMD_PROJECT_EDIT);

          command.executeWithChecks(new ExecutionEvent());
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    });

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.PROJECT, this);
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
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.PROJECT, this);

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
    }
  }
}
