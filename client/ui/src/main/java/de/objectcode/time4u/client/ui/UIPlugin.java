package de.objectcode.time4u.client.ui;

import java.util.Map;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.dialogs.ExceptionDialog;

/**
 * The activator class controls the plug-in life cycle
 */
public class UIPlugin extends AbstractUIPlugin
{
  /** The plug-in ID */
  public static final String PLUGIN_ID = "de.objectcode.time4u.client.ui";

  /** The shared instance */
  private static UIPlugin plugin;

  private Map<String, Image> m_images;

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
}
