package de.objectcode.time4u.client.ui.dnd;

import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.Task;

public class ProjectTaskHolder
{
  Project m_project;
  Task m_task;

  ProjectTaskHolder(final Project project, final Task task)
  {
    m_project = project;
    m_task = task;
  }

  public Project getProject()
  {
    return m_project;
  }

  public Task getTask()
  {
    return m_task;
  }

}
