package de.objectcode.time4u.client.store.api;

import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;

/**
 * Client data repository interface.
 * 
 * @author junglas
 */
public interface IRepository
{
  /** Get the project repository. */
  IProjectRepository getProjectRepository();

  void addRepositoryListener(RepositoryEventType eventType, IRepositoryListener listener);

  void removeRepositoryListener(RepositoryEventType eventType, IRepositoryListener listener);
}
