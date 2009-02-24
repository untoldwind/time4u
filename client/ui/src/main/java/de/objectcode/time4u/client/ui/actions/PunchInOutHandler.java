package de.objectcode.time4u.client.ui.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class PunchInOutHandler extends AbstractHandler
{

  public Object execute(final ExecutionEvent event) throws ExecutionException
  {
    if (UIPlugin.getDefault().isPunchedIn()) {
      UIPlugin.getDefault().punchOut();
      return null;
    }

    final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

    if (workbenchWindow == null) {
      return null;
    }

    final ISelectionService selectionService = workbenchWindow.getSelectionService();
    final ISelection selection = selectionService.getSelection();

    if (selection == null || !(selection instanceof IAdaptable)) {
      return null;
    }

    final ProjectSummary project = (ProjectSummary) ((IAdaptable) selection).getAdapter(ProjectSummary.class);
    final TaskSummary task = (TaskSummary) ((IAdaptable) selection).getAdapter(TaskSummary.class);
    final TodoSummary todo = (TodoSummary) ((IAdaptable) selection).getAdapter(TodoSummary.class);

    if (project == null || task == null) {
      return null;
    }

    UIPlugin.getDefault().punchIn(project, task, todo);

    return null;
  }

}
