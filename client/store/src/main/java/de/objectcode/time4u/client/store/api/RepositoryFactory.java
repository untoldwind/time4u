package de.objectcode.time4u.client.store.api;

import de.objectcode.time4u.client.store.StorePlugin;

public class RepositoryFactory
{
  public static IRepository getRepository()
  {
    return StorePlugin.getDefault().getRepository();
  }
}
