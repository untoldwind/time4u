package de.objectcode.time4u.client.ui.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class TodoListContentProvider implements IStructuredContentProvider
{
  public static String EMPTY = "[none]";

  private final ITodoRepository m_todoRepository;

  public TodoListContentProvider(final ITodoRepository todoRepository)
  {
    m_todoRepository = todoRepository;
  }

  public Object[] getElements(final Object inputElement)
  {
    if (inputElement instanceof TaskSummary) {
      final TaskSummary task = (TaskSummary) inputElement;

      final List<Object> result = new ArrayList<Object>();

      result.add(EMPTY);

      try {
        result.addAll(m_todoRepository.getTodoSummaries(TodoFilter.filterTodoForTask(task.getId())));
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }

      return result.toArray();
    }
    return new Object[] { EMPTY };
  }

  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
  {
  }

  public void dispose()
  {
  }
}
