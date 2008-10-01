package de.objectcode.time4u.client.store.api;

import de.objectcode.time4u.client.store.StorePlugin;
import de.objectcode.time4u.client.store.api.meta.MetaRepository;

/**
 * Main factory for the client side repository API.
 * 
 * All other plugins should use this factory to obtain an implementation of the repository interfaces.
 * 
 * @author junglas
 */
public class RepositoryFactory
{
  /**
   * Get the instance of the main data repository.
   * 
   * @return The data repository
   */
  public static IRepository getRepository()
  {
    return StorePlugin.getDefault().getRepository();
  }

  /**
   * Get an instance of the meta data definition repository.
   * 
   * @return The meta data definition repository.
   */
  public static MetaRepository getMetaRepository()
  {
    return StorePlugin.getDefault().getMetaRepository();
  }
}
