package de.objectcode.time4u.client.store.backend;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.objectcode.time4u.client.store.backend.preferences.IPreferencesConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class StoreBackendPlugin extends AbstractUIPlugin
{

  // The plug-in ID
  public static final String PLUGIN_ID = "de.objectcode.time4u.client.store.backend";

  // The shared instance
  private static StoreBackendPlugin plugin;

  /**
   * The constructor
   */
  public StoreBackendPlugin()
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

  public IDatabaseBackend getDatabaseBackend() throws Exception
  {

    final IExtensionRegistry registry = Platform.getExtensionRegistry();
    final IExtensionPoint extensionPoint = registry
        .getExtensionPoint("de.objectcode.time4u.client.store.backend.backend");

    final String databaseBackendId = getPreferenceStore().getString(IPreferencesConstants.STORE_DATABASE_BACKEND);

    for (final IConfigurationElement element : extensionPoint.getConfigurationElements()) {
      final String id = element.getAttribute("id");

      if (databaseBackendId.equals(id)) {
        return (IDatabaseBackend) element.createExecutableExtension("class");
      }
    }

    return null;
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static StoreBackendPlugin getDefault()
  {
    return plugin;
  }
}
