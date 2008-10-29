package de.objectcode.time4u.client.connection.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.objectcode.time4u.client.connection.ui.dialogs.ExceptionDialog;
import de.objectcode.time4u.client.connection.ui.jobs.SynchronizeJob;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.server.api.data.ServerConnection;

/**
 * The activator class controls the plug-in life cycle
 */
public class ConnectionUIPlugin extends AbstractUIPlugin implements IRepositoryListener
{

  // The plug-in ID
  public static final String PLUGIN_ID = "de.objectcode.client.time4u.connection.ui";

  // The shared instance
  private static ConnectionUIPlugin plugin;

  private ResourceBundle m_resourceBundle;
  private Map<Long, SynchronizeJob> m_synchronizeJobs;

  /**
   * The constructor
   */
  public ConnectionUIPlugin()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start(final BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;

    m_synchronizeJobs = new HashMap<Long, SynchronizeJob>();

    for (final ServerConnection serverConnection : RepositoryFactory.getRepository().getServerConnectionRepository()
        .getServerConnections()) {
      final SynchronizeJob job = new SynchronizeJob(serverConnection);

      m_synchronizeJobs.put(serverConnection.getId(), job);
    }
    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.SERVER_CONNECTION, this);

    synchronized (m_synchronizeJobs) {
      for (final SynchronizeJob job : m_synchronizeJobs.values()) {
        job.reschedule();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(final BundleContext context) throws Exception
  {
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.SERVER_CONNECTION, this);
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static ConnectionUIPlugin getDefault()
  {
    return plugin;
  }

  public SynchronizeJob getSynchronizeJob(final long serverConnectionId)
  {
    synchronized (m_synchronizeJobs) {
      return m_synchronizeJobs.get(serverConnectionId);
    }
  }

  public String getString(final String key)
  {
    if (m_resourceBundle == null) {
      m_resourceBundle = Platform.getResourceBundle(getBundle());
    }

    return m_resourceBundle.getString(key);
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

  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    synchronized (m_synchronizeJobs) {
      try {
        m_synchronizeJobs.clear();

        for (final ServerConnection serverConnection : RepositoryFactory.getRepository()
            .getServerConnectionRepository().getServerConnections()) {
          final SynchronizeJob job = new SynchronizeJob(serverConnection);

          m_synchronizeJobs.put(serverConnection.getId(), job);
        }
      } catch (final Exception e) {
        log(e);
      }
      for (final SynchronizeJob job : m_synchronizeJobs.values()) {
        job.reschedule();
      }
    }
  }

}
