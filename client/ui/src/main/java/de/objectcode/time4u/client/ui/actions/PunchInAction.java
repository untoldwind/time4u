package de.objectcode.time4u.client.ui.actions;

import java.util.UUID;

import org.eclipse.jface.action.Action;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.ui.ICommandIds;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;

public class PunchInAction extends Action
{
  private final IProjectRepository m_projectRepository;
  private final ITaskRepository m_taskRepository;
  private final UUID m_projectId;
  private final UUID m_taskId;

  public PunchInAction(final IRepository repository, final UUID projectId, final UUID taskId)
  {
    m_projectRepository = repository.getProjectRepository();
    m_taskRepository = repository.getTaskRepository();
    m_projectId = projectId;
    m_taskId = taskId;
    setId(ICommandIds.CMD_PUNCHIN);
    try {
      final ProjectSummary project = m_projectRepository.getProjectSummary(m_projectId);
      final TaskSummary task = m_taskRepository.getTaskSummary(m_taskId);

      setText(project.getName() + " -> " + task.getName());
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
    setActionDefinitionId(ICommandIds.CMD_PUNCHIN);
    setImageDescriptor(UIPlugin.getImageDescriptor("/icons/PunchedIn.gif"));
  }

  public UUID getProjectId()
  {
    return m_projectId;
  }

  public UUID getTaskId()
  {
    return m_taskId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEnabled()
  {
    try {
      final ProjectSummary project = m_projectRepository.getProjectSummary(m_projectId);
      final TaskSummary task = m_taskRepository.getTaskSummary(m_taskId);

      return project != null && project.isActive() && task != null && task.isActive();
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run()
  {
    if (UIPlugin.getDefault().isPunchedIn()) {
      UIPlugin.getDefault().punchOut();
    }

    try {
      final ProjectSummary project = m_projectRepository.getProjectSummary(m_projectId);
      final TaskSummary task = m_taskRepository.getTaskSummary(m_taskId);

      UIPlugin.getDefault().punchIn(project, task);
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
  }
}