package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.ITodoRepository;

public class TodoContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
  private final ITodoRepository m_todoRepository;

  public TodoContentProvider(final ITodoRepository todoRepository)
  {
    m_todoRepository = todoRepository;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getChildren(final Object parentElement)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public Object getParent(final Object element)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasChildren(final Object element)
  {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getElements(final Object inputElement)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public void dispose()
  {
  }

  /**
   * {@inheritDoc}
   */
  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
  {
  }
}
