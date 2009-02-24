package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class TodoTableContentProvider implements IStructuredContentProvider
{
  private final ITodoRepository m_todoRepository;

  public TodoTableContentProvider(final ITodoRepository todoRepository)
  {
    m_todoRepository = todoRepository;
  }

  public Object[] getElements(final Object inputElement)
  {
    try {
      final TodoFilter filter = new TodoFilter(false, null, null, null, TodoFilter.Order.HEADER);
      filter.setGroup(false);

      return m_todoRepository.getTodoSummaries(filter).toArray();
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    return new Object[0];
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
