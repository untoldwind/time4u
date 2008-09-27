package de.objectcode.time4u.client.store;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.meta.MetaRepository;
import de.objectcode.time4u.client.store.impl.hibernate.HibernateRepository;

/**
 * The activator class controls the plug-in life cycle
 */
public class StorePlugin extends Plugin
{

  /** The plug-in ID */
  public static final String PLUGIN_ID = "de.objectcode.time4u.store";

  /** The shared instance */
  private static StorePlugin plugin;

  private IRepository m_repository;
  private MetaRepository m_metaRepository;

  /**
   * The constructor
   */
  public StorePlugin()
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

    m_repository = new HibernateRepository(getStateLocation().toFile());
    m_metaRepository = new MetaRepository();
  }

  /**
   * {@inheritDoc}
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
  public static StorePlugin getDefault()
  {
    return plugin;
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

  public IRepository getRepository()
  {
    return m_repository;
  }

  public MetaRepository getMetaRepository()
  {
    return m_metaRepository;
  }

}
