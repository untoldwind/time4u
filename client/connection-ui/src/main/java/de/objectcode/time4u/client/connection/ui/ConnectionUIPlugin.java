package de.objectcode.time4u.client.connection.ui;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.objectcode.time4u.client.connection.ui.dialogs.ExceptionDialog;

/**
 * The activator class controls the plug-in life cycle
 */
public class ConnectionUIPlugin extends AbstractUIPlugin
{

  // The plug-in ID
  public static final String PLUGIN_ID = "de.objectcode.client.time4u.connection.ui";

  // The shared instance
  private static ConnectionUIPlugin plugin;

  /**
   * The constructor
   */
  public ConnectionUIPlugin()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(final BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(final BundleContext context) throws Exception
  {
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

}
