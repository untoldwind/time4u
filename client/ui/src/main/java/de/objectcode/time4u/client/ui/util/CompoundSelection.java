package de.objectcode.time4u.client.ui.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;

public class CompoundSelection implements ISelection, IAdaptable
{
  Map<CompoundSelectionEntityType, Object> m_selections = new HashMap<CompoundSelectionEntityType, Object>();

  public synchronized Object getSelection(final CompoundSelectionEntityType type)
  {
    return m_selections.get(type);
  }

  public synchronized void setSelection(final CompoundSelectionEntityType type, final Object selection)
  {
    if (selection == null) {
      m_selections.remove(type);
    } else {
      m_selections.put(type, selection);
    }
  }

  public boolean isEmpty()
  {
    return m_selections.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Class adapter)
  {
    return Platform.getAdapterManager().getAdapter(this, adapter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("MultiEntitySelection(");
    buffer.append(m_selections).append(")");
    return buffer.toString();
  }

}
