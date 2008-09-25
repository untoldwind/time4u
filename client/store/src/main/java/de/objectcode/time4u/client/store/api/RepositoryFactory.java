package de.objectcode.time4u.client.store.api;

import de.objectcode.time4u.client.store.StorePlugin;
import de.objectcode.time4u.client.store.api.meta.MetaRepository;

public class RepositoryFactory
{
  public static IRepository getRepository()
  {
    return StorePlugin.getDefault().getRepository();
  }

  public static MetaRepository getMetaRepository()
  {
    return StorePlugin.getDefault().getMetaRepository();
  }
}
