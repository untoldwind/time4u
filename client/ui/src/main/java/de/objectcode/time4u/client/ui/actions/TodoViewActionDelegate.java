package de.objectcode.time4u.client.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import de.objectcode.time4u.client.ui.views.TodoTreeView;
import de.objectcode.time4u.client.ui.views.TodoTreeView.ViewType;

public class TodoViewActionDelegate implements IViewActionDelegate
{
  TodoTreeView m_view;

  /**
   * {@inheritDoc}
   */
  public void init(final IViewPart view)
  {
    m_view = (TodoTreeView) view;
  }

  /**
   * {@inheritDoc}
   */
  public void run(final IAction action)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.ui.todo.view.tree".equals(id)) {
      m_view.setViewType(ViewType.TREE);
    } else if ("de.objectcode.time4u.client.ui.todo.view.flat".equals(id)) {
      m_view.setViewType(ViewType.FLAT);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void selectionChanged(final IAction action, final ISelection selection)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.ui.todo.view.tree".equals(id)) {
      action.setChecked(m_view.getViewType() == ViewType.TREE);
    } else if ("de.objectcode.time4u.client.ui.todo.view.flat".equals(id)) {
      action.setChecked(m_view.getViewType() == ViewType.FLAT);
    }
  }

}
