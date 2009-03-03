package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class TodoTreeContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
  private final ITodoRepository m_todoRepository;
  private final TodoFilterSettings m_filterSettings;

  public TodoTreeContentProvider(final ITodoRepository todoRepository, final TodoFilterSettings filterSettings)
  {
    m_todoRepository = todoRepository;
    m_filterSettings = filterSettings;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getChildren(final Object parentElement)
  {
    try {
      if (parentElement instanceof TodoSummary) {
        final TodoFilter filter = TodoFilter.filterTodos(((TodoSummary) parentElement).getId());

        m_filterSettings.apply(filter);

        return m_todoRepository.getTodoSummaries(filter, m_filterSettings.isOnlyVisible()).toArray();
      } else {
        final TodoFilter filter = TodoFilter.filterRootTodos();

        m_filterSettings.apply(filter);

        return m_todoRepository.getTodoSummaries(filter, m_filterSettings.isOnlyVisible()).toArray();
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
    if (element instanceof TodoSummary) {
      return ((TodoSummary) element).isGroup();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getElements(final Object inputElement)
  {
    return getChildren(inputElement);
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
