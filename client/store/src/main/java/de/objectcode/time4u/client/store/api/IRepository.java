package de.objectcode.time4u.client.store.api;

import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.server.api.data.Person;

/**
 * Client data repository interface.
 * 
 * @author junglas
 */
public interface IRepository
{
  /** Get the project repository. */
  IProjectRepository getProjectRepository();

  /** Get the task repository. */
  ITaskRepository getTaskRepository();

  /** Get the workitem repository */
  IWorkItemRepository getWorkItemRepository();

  IServerConnectionRepository getServerConnectionRepository();

  long getClientId();

  Person getOwner();

  void addRepositoryListener(RepositoryEventType eventType, IRepositoryListener listener);

  void removeRepositoryListener(RepositoryEventType eventType, IRepositoryListener listener);
}
