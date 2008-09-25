package de.objectcode.client.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.objectcode.time4u.client.store.api.RepositoryFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class UIPlugin extends AbstractUIPlugin
{

  /** The plug-in ID */
  public static final String PLUGIN_ID = "de.objectcode.client.ui";

  /** The shared instance */
  private static UIPlugin plugin;

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

    System.out.println(RepositoryFactory.getRepository());
  }

  /**
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
}
