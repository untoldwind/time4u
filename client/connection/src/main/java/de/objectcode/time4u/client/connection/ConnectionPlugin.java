package de.objectcode.time4u.client.connection;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ConnectionPlugin extends Plugin
{

  // The plug-in ID
  public static final String PLUGIN_ID = "de.objectcode.time4u.connection";

  // The shared instance
  private static ConnectionPlugin plugin;

  /**
   * The constructor
   */
  public ConnectionPlugin()
  {
  }

  /**
   * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(final BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;
  }

  /**
   * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(final BundleContext context) throws Exception
  {
    plugin = null;
    super.stop(context);
  }

  /**
   * Log an error to the client log.
   * 
   * @param e
   *          The exception to log
   */
  public void log(final Exception e)
  {
    getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.ERROR, e.toString(), e));
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static ConnectionPlugin getDefault()
  {
    return plugin;
  }

}
