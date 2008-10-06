package de.objectcode.time4u.client.store.api;

import java.util.Map;

import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.SynchronizableType;

/**
 * Client data repository interface.
 * 
 * @author junglas
 */
public interface IRepository
{
  /** Get the person repository. */
  IPersonRepository getPersonRepository();

  /** Get the project repository. */
  IProjectRepository getProjectRepository();

  /** Get the task repository. */
  ITaskRepository getTaskRepository();

  /** Get the workitem repository */
  IWorkItemRepository getWorkItemRepository();

  /** Get the statistics repository */
  IStatisticRepository getStatisticRepository();

  IServerConnectionRepository getServerConnectionRepository();

  long getClientId();

  Person getOwner();

  Map<SynchronizableType, Long> getRevisionStatus() throws RepositoryException;

  void addRepositoryListener(RepositoryEventType eventType, IRepositoryListener listener);

  void removeRepositoryListener(RepositoryEventType eventType, IRepositoryListener listener);
}
