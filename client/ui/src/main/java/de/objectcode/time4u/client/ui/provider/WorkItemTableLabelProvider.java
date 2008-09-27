package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.IWorkItemRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.util.DateFormat;
import de.objectcode.time4u.client.ui.util.TimeFormat;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

public class WorkItemTableLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider
{
  private final IWorkItemRepository m_workItemRepository;
  private final IProjectRepository m_projectRepository;
  private final ITaskRepository m_taskRepository;

  private final boolean m_showDay;

  public WorkItemTableLabelProvider(final IRepository repository, final boolean showDay)
  {
    m_workItemRepository = repository.getWorkItemRepository();
    m_projectRepository = repository.getProjectRepository();
    m_taskRepository = repository.getTaskRepository();

    m_showDay = showDay;
  }

  public Color getBackground(final Object element)
  {
    //    return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
    return null;
  }

  public Color getForeground(final Object element)
  {
    WorkItem activeWorkItem = null;
    try {
      activeWorkItem = m_workItemRepository.getActiveWorkItem();
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    if (element instanceof WorkItem) {
      if (!((WorkItem) element).isValid()) {
        return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_RED);
      } else if (activeWorkItem != null && activeWorkItem.getId().equals(((WorkItem) element).getId())) {
        return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
      }
    }
    return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
  }

  public Image getColumnImage(final Object element, final int columnIndex)
  {
    return null;
  }

  public String getColumnText(final Object element, int columnIndex)
  {
    if (element instanceof WorkItem) {
      final WorkItem workItem = (WorkItem) element;

      if (!m_showDay) {
        columnIndex++;
      }

      switch (columnIndex) {
        case 0:
          return DateFormat.format(workItem.getDay());
        case 1:
          return String.valueOf(TimeFormat.format(workItem.getBegin()));
        case 2:
          return String.valueOf(TimeFormat.format(workItem.getEnd()));
        case 3:
          return String.valueOf(TimeFormat.format(workItem.getDuration()));
        case 4:
          try {
            final ProjectSummary project = m_projectRepository.getProjectSummary(workItem.getProjectId());
            return String.valueOf(project != null ? project.getName() : "");
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        case 5:
          try {
            final TaskSummary todo = m_taskRepository.getTaskSummary(workItem.getTaskId());
            return String.valueOf(todo != null ? todo.getName() : "");
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        case 6:
          if (workItem.getComment() == null) {
            return null;
          }
          return String.valueOf(workItem.getComment());
      }
    }

    return element.toString();
  }

}
