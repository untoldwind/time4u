package de.objectcode.time4u.client.ui.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;

public class SelectionServiceAdapter implements ISelectionProvider
{
  private final ISelectionService m_selectionService;

  private final Map<ISelectionChangedListener, SelectionListenerAdapter> m_adapters = Collections
      .synchronizedMap(new HashMap<ISelectionChangedListener, SelectionListenerAdapter>());

  public SelectionServiceAdapter(final ISelectionService selectionService)
  {
    m_selectionService = selectionService;
  }

  public void addSelectionChangedListener(final ISelectionChangedListener listener)
  {
    final SelectionListenerAdapter adapter = new SelectionListenerAdapter(listener);

    m_adapters.put(listener, adapter);
    m_selectionService.addSelectionListener(adapter);
  }

  public void removeSelectionChangedListener(final ISelectionChangedListener listener)
  {
    final SelectionListenerAdapter adapter = m_adapters.get(listener);

    if (adapter != null) {
      m_selectionService.removeSelectionListener(adapter);
      m_adapters.remove(listener);
    }
  }

  public ISelection getSelection()
  {
    return m_selectionService.getSelection();
  }

  public void setSelection(final ISelection selection)
  {
  }

  private class SelectionListenerAdapter implements ISelectionListener
  {
    private final ISelectionChangedListener m_selectionChangedListener;

    public SelectionListenerAdapter(final ISelectionChangedListener selectionChangedListener)
    {
      super();
      m_selectionChangedListener = selectionChangedListener;
    }

    public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
    {
      m_selectionChangedListener.selectionChanged(new SelectionChangedEvent(SelectionServiceAdapter.this, selection));
    }

  }
}
