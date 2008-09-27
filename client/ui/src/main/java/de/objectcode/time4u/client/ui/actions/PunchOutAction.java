package de.objectcode.time4u.client.ui.actions;

import org.eclipse.jface.action.Action;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.ICommandIds;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

public class PunchOutAction extends Action
{
  public PunchOutAction()
  {
    String text = "";
    try {
      final WorkItem workItem = RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem();

      if (workItem != null) {
        final ProjectSummary project = RepositoryFactory.getRepository().getProjectRepository().getProjectSummary(
            workItem.getProjectId());
        final TaskSummary task = RepositoryFactory.getRepository().getTaskRepository().getTaskSummary(
            workItem.getTaskId());

        text = " - " + project.getName() + " -> " + task.getName();
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
    setId(ICommandIds.CMD_PUNCHOUT);
    setText(UIPlugin.getDefault().getString("action.punchOut.label") + text);
    setActionDefinitionId(ICommandIds.CMD_PUNCHOUT);
    setImageDescriptor(UIPlugin.getImageDescriptor("/icons/PunchedOut.gif"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEnabled()
  {
    return UIPlugin.getDefault().isPunchedIn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run()
  {
    UIPlugin.getDefault().punchOut();
  }

}
