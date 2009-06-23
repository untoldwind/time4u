package de.objectcode.time4u.client.ui.actions;

import java.util.Collection;
import java.util.Date;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.dialogs.PersonListDialog;
import de.objectcode.time4u.client.ui.dialogs.TeamListDialog;
import de.objectcode.time4u.client.ui.dialogs.TodoAssignmentDialog;
import de.objectcode.time4u.client.ui.dialogs.TodoCompleteDialog;
import de.objectcode.time4u.client.ui.dialogs.TodoDialog;
import de.objectcode.time4u.client.ui.dialogs.TodoGroupDialog;
import de.objectcode.time4u.client.ui.preferences.PreferenceConstants;
import de.objectcode.time4u.client.ui.views.TodoTreeView;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoAssignment;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

public class TodoActionDelegate implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
  IWorkbenchWindow m_window;
  IShellProvider m_shellProvider;

  IAdaptable m_selection;

  /**
   * {@inheritDoc}
   */
  public void init(final IViewPart view)
  {
    m_shellProvider = view.getSite();
    m_window = view.getSite().getWorkbenchWindow();
  }

  /**
   * {@inheritDoc}
   */
  public void init(final IWorkbenchWindow window)
  {
    m_window = window;
    m_shellProvider = new SameShellProvider(window.getShell());
  }

  /**
   * {@inheritDoc}
   */
  public void run(final IAction action)
  {
    final String id = action.getId();

    if ("de.objectcode.time4u.client.ui.todo.new".equals(id)) {
      final TaskSummary task = (TaskSummary) m_selection.getAdapter(TaskSummary.class);
      final TodoSummary todo = (TodoSummary) m_selection.getAdapter(TodoSummary.class);
      final TodoDialog dialog = new TodoDialog(m_shellProvider, task, todo);

      if (dialog.open() == TodoDialog.OK) {
        try {
          RepositoryFactory.getRepository().getTodoRepository().storeTodo(dialog.getTodo(), true);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todoGroup.new".equals(id)) {
      final TodoSummary todo = (TodoSummary) m_selection.getAdapter(TodoSummary.class);
      final TodoGroupDialog dialog = new TodoGroupDialog(m_shellProvider, todo);

      if (dialog.open() == TodoGroupDialog.OK) {
        try {
          RepositoryFactory.getRepository().getTodoRepository().storeTodoGroup(dialog.getTodoGroup(), true);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todo.edit".equals(id)) {
      final TodoSummary selection = (TodoSummary) m_selection.getAdapter(TodoSummary.class);

      if (selection != null) {
        if (selection.isGroup()) {
          try {
            final TodoGroup todoGroup = RepositoryFactory.getRepository().getTodoRepository().getTodoGroup(
                selection.getId());
            final TodoGroupDialog dialog = new TodoGroupDialog(m_shellProvider, todoGroup, false);

            if (dialog.open() == TodoGroupDialog.OK) {
              RepositoryFactory.getRepository().getTodoRepository().storeTodoGroup(dialog.getTodoGroup(), true);
            }
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        } else {
          try {
            final Todo todo = RepositoryFactory.getRepository().getTodoRepository().getTodo(selection.getId());
            final TodoDialog dialog = new TodoDialog(m_shellProvider, todo);

            if (dialog.open() == TodoDialog.OK) {
              RepositoryFactory.getRepository().getTodoRepository().storeTodo(dialog.getTodo(), true);
            }
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todo.delete".equals(id)) {
      final TodoSummary selection = (TodoSummary) m_selection.getAdapter(TodoSummary.class);

      if (selection != null) {
        try {
          if (selection.isGroup()) {
            final TodoFilter filter = new TodoFilter();
            filter.setDeleted(false);
            filter.setGroupId(selection.getId());

            final Collection<TodoSummary> todos = RepositoryFactory.getRepository().getTodoRepository()
                .getTodoSummaries(filter, false);

            if (todos != null && todos.size() > 0) {
              MessageDialog.openInformation(m_shellProvider.getShell(), "Todo delete",
                  "Can't delete todo groups with children");

              return;
            }
          }
          final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();

          if (store.getBoolean(PreferenceConstants.UI_CONFIRM_TODO_DELETE)) {
            if (!MessageDialog.openQuestion(m_shellProvider.getShell(), "Todo delete", "Delete Todo '"
                + selection.getHeader() + "'")) {
              return;
            }
          }

          RepositoryFactory.getRepository().getTodoRepository().deleteTodo(selection);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todo.assign".equals(id)) {
      final Todo selection = (Todo) m_selection.getAdapter(Todo.class);

      if (selection != null && selection.getAssignments() != null) {
        final String selfId = RepositoryFactory.getRepository().getOwner().getId();
        for (final TodoAssignment assignment : selection.getAssignments()) {
          if (selfId.equals(assignment.getPersonId())) {
            // Already assigned
            return;
          }
        }
        final TodoAssignmentDialog dialog = new TodoAssignmentDialog(m_shellProvider, selection);

        if (dialog.open() == TodoAssignmentDialog.OK) {
          selection.getAssignments().add(dialog.getTodoAssignment());
          if (selection.getState() == TodoState.UNASSIGNED) {
            selection.setState(TodoState.ASSIGNED_OPEN);
          }

          try {
            RepositoryFactory.getRepository().getTodoRepository().storeTodo(selection, true);
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todo.work".equals(id)) {
      final Todo selection = (Todo) m_selection.getAdapter(Todo.class);

      if (selection != null && selection.getTaskId() != null && selection.getAssignments() != null) {
        try {
          final TaskSummary task = RepositoryFactory.getRepository().getTaskRepository().getTaskSummary(
              selection.getTaskId());
          final ProjectSummary project = RepositoryFactory.getRepository().getProjectRepository().getProjectSummary(
              task.getProjectId());

          final String selfId = RepositoryFactory.getRepository().getOwner().getId();
          boolean assigned = false;
          for (final TodoAssignment assignment : selection.getAssignments()) {
            if (selfId.equals(assignment.getPersonId())) {
              assigned = true;
              break;
            }
          }
          if (!assigned) {
            final TodoAssignmentDialog dialog = new TodoAssignmentDialog(m_shellProvider, selection);

            if (dialog.open() == TodoAssignmentDialog.OK) {
              selection.getAssignments().add(dialog.getTodoAssignment());
              if (selection.getState() == TodoState.UNASSIGNED || selection.getState() == TodoState.ASSIGNED_OPEN) {
                selection.setState(TodoState.ASSIGNED_INPROGRESS);
              }

              try {
                RepositoryFactory.getRepository().getTodoRepository().storeTodo(selection, true);
              } catch (final Exception e) {
                UIPlugin.getDefault().log(e);
              }

              UIPlugin.getDefault().punchIn(project, task, selection);
            }
          } else {
            if (selection.getState() == TodoState.UNASSIGNED || selection.getState() == TodoState.ASSIGNED_OPEN) {
              try {
                selection.setState(TodoState.ASSIGNED_INPROGRESS);

                RepositoryFactory.getRepository().getTodoRepository().storeTodo(selection, true);
              } catch (final Exception e) {
                UIPlugin.getDefault().log(e);
              }
            }

            UIPlugin.getDefault().punchIn(project, task, selection);
          }
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todo.complete".equals(id)) {
      final TodoSummary selection = (TodoSummary) m_selection.getAdapter(TodoSummary.class);

      if (selection != null && !selection.isCompleted()) {
        final TodoCompleteDialog dialog = new TodoCompleteDialog(m_shellProvider, selection);
        final int result = dialog.open();

        if (result == TodoCompleteDialog.COMPLETE || result == TodoCompleteDialog.REJECT) {
          try {
            if (selection.isGroup()) {
              final TodoGroup todoGroup = RepositoryFactory.getRepository().getTodoRepository().getTodoGroup(
                  selection.getId());

              if (todoGroup != null) {
                if (result == TodoCompleteDialog.COMPLETE) {
                  todoGroup.setState(TodoState.COMPLETED);
                } else {
                  todoGroup.setState(TodoState.REJECTED);
                }
                todoGroup.setCompleted(true);
                todoGroup.setCompletedAt(new Date());

                RepositoryFactory.getRepository().getTodoRepository().storeTodoGroup(todoGroup, true);
              }
            } else {
              final Todo todo = RepositoryFactory.getRepository().getTodoRepository().getTodo(selection.getId());

              if (todo != null) {
                if (result == TodoCompleteDialog.COMPLETE) {
                  todo.setState(TodoState.COMPLETED);
                } else {
                  todo.setState(TodoState.REJECTED);
                }
                todo.setCompleted(true);
                todo.setCompletedAt(new Date());

                RepositoryFactory.getRepository().getTodoRepository().storeTodo(todo, true);
              }
            }
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      }
    } else if ("de.objectcode.time4u.client.ui.todo.person".equals(id)) {
      final PersonListDialog dialog = new PersonListDialog(m_shellProvider);

      dialog.open();
    } else if ("de.objectcode.time4u.client.ui.todo.team".equals(id)) {
      final TeamListDialog dialog = new TeamListDialog(m_shellProvider);

      dialog.open();
    } else if ("de.objectcode.time4u.client.ui.todo.showView".equals(id)) {
      try {
        m_window.getActivePage().showView(TodoTreeView.ID, null, IWorkbenchPage.VIEW_ACTIVATE);
      } catch (final PartInitException e) {
        MessageDialog.openError(m_shellProvider.getShell(), "Error", "Error opening view:" + e.getMessage());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void selectionChanged(final IAction action, final ISelection selection)
  {
    if (selection instanceof IAdaptable) {
      m_selection = (IAdaptable) selection;
    } else if (selection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) selection).getFirstElement();

      if (obj != null && obj instanceof IAdaptable) {
        m_selection = (IAdaptable) obj;
      }
    }
  }

  public void dispose()
  {
  }
}
