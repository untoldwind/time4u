package de.objectcode.time4u.server.web.ui.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.objectcode.time4u.server.ejb.seam.api.IProjectServiceLocal;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;

public class ProjectTaskSelection implements Serializable
{
  private static final long serialVersionUID = 6642983860325975206L;

  private LinkedList<ProjectBean> m_projectStack = new LinkedList<ProjectBean>();
  private Map<String, ProjectBean> m_nextProjects;
  private Map<String, TaskBean> m_tasks;
  private String m_nextProjectId;
  private String m_selectedTaskId;

  String m_viewId;
  IProjectServiceLocal m_projectService;

  public ProjectTaskSelection(final String viewId, final IProjectServiceLocal projectService)
  {
    m_viewId = viewId;
    m_projectService = projectService;
  }

  public String getNextProjectId()
  {
    return m_nextProjectId;
  }

  public void setNextProjectId(final String fromNextProjectId)
  {
    m_nextProjectId = fromNextProjectId;
  }

  public List<ProjectBean> getProjectStack()
  {
    return m_projectStack;
  }

  public List<ProjectBean> getNextProjects()
  {
    if (m_nextProjects == null) {
      final Map<String, ProjectBean> nextProjects = new TreeMap<String, ProjectBean>();
      List<ProjectEntity> projects;

      if (m_projectStack.isEmpty()) {
        projects = m_projectService.getRootProjects();
      } else {
        projects = m_projectService.getChildProjects(m_projectStack.getLast().getId());
      }

      for (final ProjectEntity project : projects) {
        nextProjects.put(project.getId(), new ProjectBean(project));
      }

      m_nextProjects = nextProjects;
    }
    return new ArrayList<ProjectBean>(m_nextProjects.values());
  }

  public String getSelectedTaskId()
  {
    return m_selectedTaskId;
  }

  public void setSelectedTaskId(final String selectedTaskId)
  {
    m_selectedTaskId = selectedTaskId;
  }

  public List<TaskBean> getTasks()
  {
    if (m_tasks == null) {
      final Map<String, TaskBean> nextTasks = new TreeMap<String, TaskBean>();
      if (!m_projectStack.isEmpty()) {
        final List<TaskEntity> tasks = m_projectService.getTasks(m_projectStack.getLast().getId());

        for (final TaskEntity task : tasks) {
          nextTasks.put(task.getId(), new TaskBean(task));
        }

      }
      m_tasks = nextTasks;
    }

    return new ArrayList<TaskBean>(m_tasks.values());
  }

  public String nextProject()
  {
    if (m_nextProjects != null) {
      final ProjectBean project = m_nextProjects.get(m_nextProjectId);

      if (project != null) {
        m_projectStack.add(project);
      }
      m_tasks = null;
      m_selectedTaskId = null;
      m_nextProjects = null;
      m_nextProjectId = null;
      return m_viewId;
    }
    return m_viewId;
  }

  public String setProject(final String projectId)
  {
    final LinkedList<ProjectBean> newStack = new LinkedList<ProjectBean>();

    for (final ProjectBean project : m_projectStack) {
      newStack.add(project);

      if (project.getId().equals(projectId)) {
        break;
      }
    }
    m_tasks = null;
    m_selectedTaskId = null;
    m_projectStack = newStack;
    m_nextProjects = null;
    m_nextProjectId = null;
    return m_viewId;
  }

  public String clearProject()
  {
    m_tasks = null;
    m_selectedTaskId = null;
    m_projectStack = new LinkedList<ProjectBean>();
    m_nextProjects = null;
    m_nextProjectId = null;
    return m_viewId;
  }

  public static class ProjectBean implements Serializable
  {
    private static final long serialVersionUID = -5934658867783123094L;

    String m_id;
    String m_name;
    boolean m_active;

    public ProjectBean(final ProjectEntity project)
    {
      m_id = project.getId();
      m_name = project.getName();
      m_active = project.isActive();
    }

    public String getId()
    {
      return m_id;
    }

    public String getName()
    {
      return m_name;
    }

    public boolean isActive()
    {
      return m_active;
    }
  }

  public static class TaskBean implements Serializable
  {
    private static final long serialVersionUID = -5934658867783123094L;

    String m_id;
    String m_name;
    boolean m_active;

    public TaskBean(final TaskEntity task)
    {
      m_id = task.getId();
      m_name = task.getName();
      m_active = task.isActive();
    }

    public String getId()
    {
      return m_id;
    }

    public String getName()
    {
      return m_name;
    }

    public boolean isActive()
    {
      return m_active;
    }
  }
}
