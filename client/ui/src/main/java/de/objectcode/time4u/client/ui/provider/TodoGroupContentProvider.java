package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class TodoGroupContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
  private final ITodoRepository m_todoRepository;

  public final static String ROOT = "[Root]";

  public TodoGroupContentProvider(final ITodoRepository todoRepository)
  {
    m_todoRepository = todoRepository;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getChildren(final Object parentElement)
  {
    try {
      if (parentElement instanceof TodoSummary) {
        return m_todoRepository.getTodoSummaries(TodoFilter.filterTodoGroups(((TodoSummary) parentElement).getId()))
            .toArray();
      } else {
        return m_todoRepository.getTodoSummaries(TodoFilter.filterRootTodoGroups()).toArray();
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    return new Object[0];
  }

  /**
   * {@inheritDoc}
   */
  public Object getParent(final Object element)
  {
    try {
      if (element instanceof TodoSummary) {
        if (((TodoSummary) element).getGroupdId() != null) {
          return m_todoRepository.getTodoSummary(((TodoSummary) element).getGroupdId());
        } else {
          return ROOT;
        }
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasChildren(final Object element)
  {
    return getChildren(element).length > 0;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getElements(final Object inputElement)
  {
    return new Object[] { ROOT };
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
