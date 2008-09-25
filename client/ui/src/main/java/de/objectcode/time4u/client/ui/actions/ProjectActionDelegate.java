package de.objectcode.time4u.client.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.ProjectFilter;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.TaskFilter;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.dialogs.ProjectCopyDialog;
import de.objectcode.time4u.client.ui.dialogs.ProjectDeleteDialog;
import de.objectcode.time4u.client.ui.dialogs.ProjectDialog;
import de.objectcode.time4u.client.ui.preferences.PreferenceConstants;
import de.objectcode.time4u.client.ui.views.ProjectTreeView;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.Task;

public class ProjectActionDelegate implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
  IShellProvider m_shellProvider;
  ProjectTreeView m_view;

  public void init(final IWorkbenchWindow window)
  {
    m_shellProvider = new SameShellProvider(window.getShell());
    m_view = (ProjectTreeView) window.getActivePage().findView(ProjectTreeView.ID);
  }

  public void init(final IViewPart view)
  {
    m_shellProvider = view.getSite();
    if (view instanceof ProjectTreeView) {
      m_view = (ProjectTreeView) view;
    }
  }

  public void dispose()
  {
  }

  public void run(final IAction action)
  {
    final String id = action.getId();

    final Project selectedProject = m_view.getSelectedProject();

    if ("de.objectcode.time4u.client.ui.project.new".equals(id)) {
      final ProjectDialog dialog = new ProjectDialog(m_shellProvider);

      if (selectedProject != null) {
        dialog.getProject().setParentId(selectedProject.getId());
      }

      if (dialog.open() == ProjectDialog.OK) {
        try {
          RepositoryFactory.getRepository().getProjectRepository().storeProject(dialog.getProject());
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.ui.project.edit".equals(id)) {
      if (selectedProject != null) {
        final ProjectDialog dialog = new ProjectDialog(m_shellProvider, selectedProject);

        if (dialog.open() == ProjectDialog.OK) {
          try {
            RepositoryFactory.getRepository().getProjectRepository().storeProject(dialog.getProject());
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      }
    } else if ("de.objectcode.time4u.client.ui.project.copy".equals(id)) {
      final ProjectCopyDialog dialog = new ProjectCopyDialog(m_shellProvider, selectedProject);

      if (dialog.open() == ProjectDialog.OK) {
        copyProject(selectedProject, dialog.getNewName(), dialog.getNewParent(), dialog.isCopyTasks(), dialog
            .isCopySubProjects());
      }
    } else if ("de.objectcode.time4u.client.project.delete".equals(id)) {
      if (selectedProject != null) {
        try {
          final ProjectFilter filter = new ProjectFilter();
          filter.setDeleted(false);
          filter.setParentProject(selectedProject.getId());
          final Collection<Project> children = RepositoryFactory.getRepository().getProjectRepository().getProjects(
              filter);

          if (children != null && children.size() > 0) {
            MessageDialog.openInformation(m_shellProvider.getShell(), "Project delete",
                "Can't delete project that has child projects");

            return;
          }
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
        try {
          final TaskFilter filter = new TaskFilter();
          filter.setDeleted(false);
          filter.setProject(selectedProject.getId());
          final Collection<Task> tasks = RepositoryFactory.getRepository().getTaskRepository().getTasks(filter);

          if (tasks != null && tasks.size() > 0) {
            MessageDialog.openInformation(m_shellProvider.getShell(), "Project delete",
                "Can't delete project that has tasks");

            return;
          }
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }

        final ProjectDeleteDialog warningDialog = new ProjectDeleteDialog(m_shellProvider, selectedProject);

        if (warningDialog.isWarningNecessary()) {
          if (warningDialog.open() != ProjectDeleteDialog.OK) {
            return;
          }
        } else {
          final IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();

          if (store.getBoolean(PreferenceConstants.UI_CONFIRM_PROJECT_DELETE)) {
            if (!MessageDialog.openQuestion(m_shellProvider.getShell(), "Project delete", "Delete Project '"
                + selectedProject.getName() + "'")) {
              return;
            }
          }
        }

        try {
          RepositoryFactory.getRepository().getProjectRepository().deleteProject(selectedProject);
        } catch (final Exception e) {
          UIPlugin.getDefault().log(e);
        }
      }
    } else if ("de.objectcode.time4u.client.project.onlyActive".equals(id)) {
      if (m_view != null) {
        m_view.setShowOnlyActive(action.isChecked());
      }
    }
  }

  public void selectionChanged(final IAction action, final ISelection selection)
  {
    Project selectedProject = null;

    if (selection instanceof IStructuredSelection) {
      final Object obj = ((IStructuredSelection) selection).getFirstElement();

      if (obj != null) {
        if (obj instanceof Project) {
          selectedProject = (Project) obj;
        }
      }
    }
    final String id = action.getId();

    if ("de.objectcode.time4u.client.project.onlyActive".equals(action.getId())) {
      if (m_view != null) {
        action.setChecked(m_view.isShowOnlyActive());
      }
    } else if ("de.objectcode.time4u.client.project.edit".equals(id)) {
      action.setEnabled(selectedProject != null);
    } else if ("de.objectcode.time4u.client.project.delete".equals(id)) {
      action.setEnabled(selectedProject != null);
    }
  }

  private void copyProject(final Project project, final String newName, final Project newParent,
      final boolean copyTasks, final boolean copySubProjects)
  {
    final Project newProject = new Project();

    newProject.setName(newName != null ? newName : project.getName());
    newProject.setParentId(newParent != null ? newParent.getId() : null);
    newProject.setActive(project.isActive());
    newProject.setDescription(project.getDescription());

    try {
      final IProjectRepository projectRepository = RepositoryFactory.getRepository().getProjectRepository();
      final ITaskRepository taskRepository = RepositoryFactory.getRepository().getTaskRepository();

      projectRepository.storeProject(newProject);

      if (copyTasks) {
        final List<Task> newTasks = new ArrayList<Task>();
        final TaskFilter taskFilter = new TaskFilter();
        taskFilter.setDeleted(false);
        taskFilter.setProject(project.getId());

        for (final Task task : taskRepository.getTasks(taskFilter)) {
          final Task newTask = new Task();

          newTask.setName(task.getName());
          newTask.setDescription(task.getDescription());
          newTask.setProjectId(newProject.getId());
          newTask.setActive(task.isActive());

          newTasks.add(newTask);
        }
        taskRepository.storeTask(newTasks);
      }

      if (copySubProjects) {
        final ProjectFilter projectFilter = new ProjectFilter();
        projectFilter.setDeleted(false);
        projectFilter.setParentProject(project.getId());
        for (final Project toCopy : projectRepository.getProjects(projectFilter)) {
          copyProject(toCopy, null, newProject, copyTasks, copySubProjects);
        }
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
  }
}
