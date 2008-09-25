package de.objectcode.time4u.client.ui.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.ui.util.MultiEntitySelectionProvider;

public class WorkItemView extends ViewPart implements IRepositoryListener, ISelectionListener
{
  public static final String ID = "de.objectcode.client.ui.view.workItemListView";

  public enum ViewType
  {
    FLAT
  };

  private TableViewer m_tableViewer;
  private ViewType m_activeViewType;

  private final int m_refreshCounter = 0;

  private MultiEntitySelectionProvider m_selectionProvider;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_selectionProvider = new MultiEntitySelectionProvider();
    getSite().setSelectionProvider(m_selectionProvider);
    getSite().getPage().addSelectionListener(m_selectionProvider);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus()
  {
    // TODO Auto-generated method stub

  }

  /**
   * {@inheritDoc}
   */
  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    // TODO Auto-generated method stub

  }

  /**
   * {@inheritDoc}
   */
  public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
  {
    // TODO Auto-generated method stub

  }

}
