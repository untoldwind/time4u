package de.objectcode.time4u.client.ui;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.actions.PunchInAction;
import de.objectcode.time4u.client.ui.dialogs.ExceptionDialog;
import de.objectcode.time4u.client.ui.jobs.ActiveWorkItemJob;
import de.objectcode.time4u.client.ui.preferences.PreferenceConstants;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.WorkItem;

/**
 * The activator class controls the plug-in life cycle
 */
public class UIPlugin extends AbstractUIPlugin
{
  /** The plug-in ID */
  public static final String PLUGIN_ID = "de.objectcode.time4u.client.ui";

  /** The shared instance */
  private static UIPlugin plugin;

  private ResourceBundle m_resourceBundle;
  private final Map<String, Image> m_images = new HashMap<String, Image>();
  private final LinkedList<PunchInAction> m_taskHistory = new LinkedList<PunchInAction>();
  private boolean m_taskHistoryInitialized = false;

  private ActiveWorkItemJob m_activeWorkItemJob;

  /**
   * The constructor
   */
  public UIPlugin()
  {
  }

  /**
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(final BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;

    m_activeWorkItemJob = new ActiveWorkItemJob();
    m_activeWorkItemJob.schedule(1000L);
  }

  /**
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(final BundleContext context) throws Exception
  {
    if (m_taskHistoryInitialized) {
      final StringBuffer buffer = new StringBuffer();

      boolean first = true;

      for (final PunchInAction action : m_taskHistory) {
        if (!first) {
          buffer.append(':');
        }

        buffer.append(action.getProjectId());
        buffer.append(",");
        buffer.append(action.getTaskId());
        first = false;
      }
      getPreferenceStore().putValue(PreferenceConstants.UI_TASK_HISTORY, buffer.toString());
    }

    plugin = null;
    super.stop(context);
  }

  /**
   * Get an image from the plugin path.
   * 
   * @param path
   *          The path relative to the plugin
   * @return Image for <tt>path</tt>
   */
  public synchronized Image getImage(final String path)
  {
    Image image = m_images.get(path);

    if (image != null) {
      return image;
    }

    final ImageDescriptor imageDescriptor = getImageDescriptor(path);

    if (imageDescriptor != null) {
      image = imageDescriptor.createImage();
    } else {
      image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
    }

    m_images.put(path, image);

    return image;
  }

  public String getString(final String key)
  {
    if (m_resourceBundle == null) {
      m_resourceBundle = Platform.getResourceBundle(getBundle());
    }

    return m_resourceBundle.getString(key);
  }

  public String getMessage(final String key, final Object... arguments)
  {
    if (m_resourceBundle == null) {
      m_resourceBundle = Platform.getResourceBundle(getBundle());
    }
    final MessageFormat temp = new MessageFormat(m_resourceBundle.getString(key));

    return temp.format(arguments);
  }

  /**
   * Log an error to the client log.
   * 
   * @param e
   *          The exception to log
   */
  public void log(final Throwable e)
  {
    getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.ERROR, e.toString(), e));

    try {
      final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

      if (window != null) {
        final ExceptionDialog dialog = new ExceptionDialog(window.getShell(), "Exception", e.toString(), e);

        dialog.open();
      }
    } catch (final Throwable ex) {
      getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.ERROR, e.toString(), ex));
    }
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static UIPlugin getDefault()
  {
    return plugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in relative path
   * 
   * @param path
   *          the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(final String path)
  {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }

  public void pushTask(final ProjectSummary project, final TaskSummary task)
  {
    synchronized (m_taskHistory) {
      if (!m_taskHistoryInitialized) {
        initializeTaskHistory();
      }

      int count = 0;
      final int max = getPreferenceStore().getInt(PreferenceConstants.UI_TASK_HISTORY_SIZE);

      for (final Iterator<PunchInAction> it = m_taskHistory.iterator(); it.hasNext();) {
        final PunchInAction action = it.next();

        if (action.getTaskId() == task.getId() || count >= max) {
          it.remove();
        } else {
          count++;
        }
      }

      m_taskHistory.addFirst(new PunchInAction(RepositoryFactory.getRepository(), project.getId(), task.getId()));
    }
  }

  public Collection<PunchInAction> getTaskHistory()
  {
    synchronized (m_taskHistory) {
      if (!m_taskHistoryInitialized) {
        initializeTaskHistory();
      }
      return new ArrayList<PunchInAction>(m_taskHistory);
    }
  }

  public void initializeTaskHistory()
  {
    m_taskHistoryInitialized = true;

    final StringTokenizer t = new StringTokenizer(getPreferenceStore().getString(PreferenceConstants.UI_TASK_HISTORY),
        ":");
    final IProjectRepository projectRepository = RepositoryFactory.getRepository().getProjectRepository();
    final ITaskRepository taskRerRepository = RepositoryFactory.getRepository().getTaskRepository();

    while (t.hasMoreTokens()) {
      try {
        String idStr = t.nextToken();
        final int idx = idStr.indexOf(",");
        ProjectSummary project = null;
        if (idx >= 0) {
          final String projectId = idStr.substring(0, idx);
          project = projectRepository.getProjectSummary(projectId);

          idStr = idStr.substring(idx + 1);
        }

        final TaskSummary task = taskRerRepository.getTaskSummary(idStr);

        if (project == null && task != null && task.getProjectId() != null) {
          project = projectRepository.getProjectSummary(task.getProjectId());
        }

        if (project != null && task != null) {
          m_taskHistory.add(new PunchInAction(RepositoryFactory.getRepository(), project.getId(), task.getId()));
        }
      } catch (final Throwable e) {
        UIPlugin.getDefault().log(e);
      }
    }
  }

  public boolean isPunchedIn()
  {
    try {
      final WorkItem workItem = RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem();

      if (workItem != null) {
        return true;
      }
    } catch (final Exception e) {
      log(e);
    }

    return false;
  }

  public WorkItem getPunchedInWorkitem()
  {
    try {
      final WorkItem workItem = RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem();

      return workItem;
    } catch (final Exception e) {
      log(e);
    }

    return null;
  }

  public WorkItem punchIn(final ProjectSummary project, final TaskSummary task)
  {
    return punchIn(project, task, null);
  }

  public WorkItem punchIn(final ProjectSummary project, final TaskSummary task, final String comment)
  {
    pushTask(project, task);

    final WorkItem workItem = new WorkItem();
    final Calendar calendar = Calendar.getInstance();
    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    final int minute = calendar.get(Calendar.MINUTE);

    workItem.setBegin(3600 * hour + 60 * minute);
    workItem.setEnd(3600 * hour + 60 * minute);
    workItem.setProjectId(project.getId());
    workItem.setTaskId(task.getId());
    workItem.setDay(new CalendarDay(calendar));

    workItem.setComment(comment != null ? comment : "");

    try {
      RepositoryFactory.getRepository().getWorkItemRepository().storeWorkItem(workItem);
      RepositoryFactory.getRepository().getWorkItemRepository().setActiveWorkItem(workItem);
    } catch (final Exception e) {
      log(e);
    }
    m_activeWorkItemJob.schedule(1000L);

    return workItem;
  }

  public void punchOut()
  {
    try {
      final WorkItem workItem = RepositoryFactory.getRepository().getWorkItemRepository().getActiveWorkItem();

      if (workItem != null) {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        workItem.setEnd(3600 * hour + 60 * minute);
        RepositoryFactory.getRepository().getWorkItemRepository().setActiveWorkItem(null);
        RepositoryFactory.getRepository().getWorkItemRepository().storeWorkItem(workItem);
      }
    } catch (final Exception e) {
      log(e);
    }
  }
}
