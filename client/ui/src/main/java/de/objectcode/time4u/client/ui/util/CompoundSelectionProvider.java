package de.objectcode.time4u.client.ui.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import de.objectcode.time4u.server.api.data.CalendarDay;

public class CompoundSelectionProvider implements IPostSelectionProvider, ISelectionListener
{
  CompoundSelection m_selection;

  /**
   * Registered selection changed listeners (element type: <code>ISelectionChangedListener</code>).
   */
  private final ListenerList listeners = new ListenerList();

  /**
   * Registered post selection changed listeners.
   */
  private final ListenerList postListeners = new ListenerList();

  private final Map<CompoundSelectionEntityType, List<ISelectionProvider>> m_selectionProviders = new HashMap<CompoundSelectionEntityType, List<ISelectionProvider>>();

  public CompoundSelectionProvider()
  {
    m_selection = createInitialSelection();
  }

  /**
   * {@inheritDoc}
   */
  public void addSelectionChangedListener(final ISelectionChangedListener listener)
  {
    listeners.add(listener);
  }

  /**
   * Adds a listener for post selection changes in this multi page selection provider.
   * 
   * @param listener
   *          a selection changed listener
   * @since 3.2
   */
  public void addPostSelectionChangedListener(final ISelectionChangedListener listener)
  {
    postListeners.add(listener);
  }

  /**
   * Notifies all registered selection changed listeners that the editor's selection has changed. Only listeners
   * registered at the time this method is called are notified.
   * 
   * @param event
   *          the selection changed event
   */
  public void fireSelectionChanged()
  {
    final SelectionChangedEvent event = new SelectionChangedEvent(this, m_selection);

    final Object[] listeners = this.listeners.getListeners();
    fireEventChange(event, listeners);
  }

  /**
   * Notifies all post selection changed listeners that the editor's selection has changed.
   * 
   * @param event
   *          the event to propogate.
   * @since 3.2
   */
  public void firePostSelectionChanged()
  {
    final SelectionChangedEvent event = new SelectionChangedEvent(this, m_selection);

    final Object[] listeners = postListeners.getListeners();
    fireEventChange(event, listeners);
  }

  private void fireEventChange(final SelectionChangedEvent event, final Object[] listeners)
  {
    for (int i = 0; i < listeners.length; ++i) {
      final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
      SafeRunner.run(new SafeRunnable() {
        public void run()
        {
          l.selectionChanged(event);
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  public ISelection getSelection()
  {
    return m_selection;
  }

  /**
   * Convenient helper method for ((CompountSelection)selectionProvider.getSelection()).getSelection(type).
   * 
   * @param type
   *          The entity type.
   * @return The selection part or <tt>null</tt>
   */
  public Object getSelection(final CompoundSelectionEntityType type)
  {
    return m_selection.getSelection(type);
  }

  /**
   * {@inheritDoc}
   */
  public void removeSelectionChangedListener(final ISelectionChangedListener listener)
  {
    listeners.remove(listener);
  }

  /**
   * Removes a listener for post selection changes in this multi page selection provider.
   * 
   * @param listener
   *          a selection changed listener
   * @since 3.2
   */
  public void removePostSelectionChangedListener(final ISelectionChangedListener listener)
  {
    postListeners.remove(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void setSelection(final ISelection selection)
  {
    if (selection instanceof CompoundSelection) {
      m_selection = (CompoundSelection) selection;

      for (final Map.Entry<CompoundSelectionEntityType, List<ISelectionProvider>> entry : m_selectionProviders
          .entrySet()) {
        for (final ISelectionProvider provider : entry.getValue()) {
          provider.setSelection(new StructuredSelection(m_selection.getSelection(entry.getKey())));
        }
      }
    }
  }

  /**
   * Change a part of the selection. This method also fires a selection change event.
   * 
   * @param type
   *          The entity type
   * @param selection
   *          The new selection part
   */
  public void changeSelection(final CompoundSelectionEntityType type, final Object selection)
  {
    m_selection.setSelection(type, selection);
    fireSelectionChanged();
  }

  public void addSelectionProvider(final CompoundSelectionEntityType type, final ISelectionProvider provider)
  {
    List<ISelectionProvider> providers = m_selectionProviders.get(type);

    if (providers == null) {
      providers = new ArrayList<ISelectionProvider>();
      m_selectionProviders.put(type, providers);
    }
    providers.add(provider);

    provider.addSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(final SelectionChangedEvent event)
      {
        final ISelection selection = event.getSelection();

        if (selection instanceof IStructuredSelection) {
          m_selection.setSelection(type, ((IStructuredSelection) selection).getFirstElement());

          fireSelectionChanged();
        }
      }
    });
  }

  public void addPostSelectionProvider(final CompoundSelectionEntityType type, final IPostSelectionProvider provider)
  {
    List<ISelectionProvider> providers = m_selectionProviders.get(type);

    if (providers == null) {
      providers = new ArrayList<ISelectionProvider>();
      m_selectionProviders.put(type, providers);
    }
    providers.add(provider);

    provider.addSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(final SelectionChangedEvent event)
      {
        final ISelection selection = event.getSelection();

        if (selection instanceof IStructuredSelection) {
          m_selection.setSelection(type, ((IStructuredSelection) selection).getFirstElement());

          fireSelectionChanged();
        }
      }
    });
    provider.addPostSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(final SelectionChangedEvent event)
      {
        final ISelection selection = event.getSelection();

        if (selection instanceof IStructuredSelection) {
          m_selection.setSelection(type, ((IStructuredSelection) selection).getFirstElement());

          firePostSelectionChanged();
        }
      }
    });
  }

  public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
  {
    if (selection instanceof CompoundSelection) {
      m_selection = (CompoundSelection) selection;
    }
  }

  /**
   * Create an initial compound selection object.
   * 
   * E.g. the initial selection for CALENDARDAY will be <tt>now</tt>
   * 
   * @return The initial compound selection
   */
  private CompoundSelection createInitialSelection()
  {
    final CompoundSelection selection = new CompoundSelection();

    selection.setSelection(CompoundSelectionEntityType.CALENDARDAY, new CalendarDay(Calendar.getInstance()));

    return selection;
  }
}
