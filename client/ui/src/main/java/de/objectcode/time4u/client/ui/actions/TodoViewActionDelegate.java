package de.objectcode.time4u.client.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import de.objectcode.time4u.client.ui.dialogs.TodoFilterDialog;
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
    } else if ("de.objectcode.time4u.client.ui.todo.filter.unassigned".equals(id)) {
      m_view.getFilterSettings().setUnassigned(!m_view.getFilterSettings().isUnassigned());
      action.setChecked(m_view.getFilterSettings().isUnassigned());
      m_view.refresh();
    } else if ("de.objectcode.time4u.client.ui.todo.filter.assignedtome".equals(id)) {
      m_view.getFilterSettings().setAssignedToMe(!m_view.getFilterSettings().isAssignedToMe());
      action.setChecked(m_view.getFilterSettings().isAssignedToMe());
      m_view.refresh();
    } else if ("de.objectcode.time4u.client.ui.todo.filter.assignedtoother".equals(id)) {
      m_view.getFilterSettings().setAssignedToOther(!m_view.getFilterSettings().isAssignedToOther());
      action.setChecked(m_view.getFilterSettings().isAssignedToOther());
      m_view.refresh();
    } else if ("de.objectcode.time4u.client.ui.todo.filter".equals(id)) {
      final TodoFilterDialog dialog = new TodoFilterDialog(m_view.getSite(), m_view.getFilterSettings());

      if (dialog.open() == TodoFilterDialog.OK) {
        m_view.refresh();
      }
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
    } else if ("de.objectcode.time4u.client.ui.todo.filter.unassigned".equals(id)) {
      action.setChecked(m_view.getFilterSettings().isUnassigned());
    } else if ("de.objectcode.time4u.client.ui.todo.filter.assignedtome".equals(id)) {
      action.setChecked(m_view.getFilterSettings().isAssignedToMe());
    } else if ("de.objectcode.time4u.client.ui.todo.filter.assignedtoother".equals(id)) {
      action.setChecked(m_view.getFilterSettings().isAssignedToOther());
    }
  }
}
