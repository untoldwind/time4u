package de.objectcode.time4u.client.ui.dnd;

import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;

public class ProjectTaskHolder
{
  private final ProjectSummary m_project;
  private final TaskSummary m_task;

  public ProjectTaskHolder(final ProjectSummary project, final TaskSummary task)
  {
    m_project = project;
    m_task = task;
  }

  public ProjectSummary getProject()
  {
    return m_project;
  }

  public TaskSummary getTask()
  {
    return m_task;
  }

}
