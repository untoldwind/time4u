package de.objectcode.time4u.client.ui.actions;

import org.eclipse.jface.action.Action;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.ui.ICommandIds;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class PunchInAction extends Action
{
  private final IProjectRepository m_projectRepository;
  private final ITaskRepository m_taskRepository;
  private final ITodoRepository m_todoRepository;
  private final String m_projectId;
  private final String m_taskId;
  private final String m_todoId;

  public PunchInAction(final IRepository repository, final String projectId, final String taskId, final String todoId)
  {
    m_projectRepository = repository.getProjectRepository();
    m_taskRepository = repository.getTaskRepository();
    m_todoRepository = repository.getTodoRepository();
    m_projectId = projectId;
    m_taskId = taskId;
    m_todoId = todoId;
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

  public String getProjectId()
  {
    return m_projectId;
  }

  public String getTaskId()
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
      TodoSummary todo = null;

      if (m_todoId != null) {
        todo = m_todoRepository.getTodoSummary(m_todoId);
      }
      UIPlugin.getDefault().punchIn(project, task, todo);
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
  }
}