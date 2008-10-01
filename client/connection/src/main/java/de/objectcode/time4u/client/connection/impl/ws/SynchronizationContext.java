package de.objectcode.time4u.client.connection.impl.ws;

import java.util.Map;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;

/**
 * Synchronization context.
 * 
 * Collection of all information necessary to perform a data synchronization using web-services.
 * 
 * @author junglas
 */
public class SynchronizationContext
{
  private final long m_serverConnectionId;
  private final Map<SynchronizableType, SynchronizationStatus> m_synchronizationStatus;
  private final Map<SynchronizableType, Long> m_clientRevisionStatus;
  Map<SynchronizableType, Long> m_serverRevisionStatus;
  private final IRepository m_repository;
  private final IProjectService m_projectService;
  private final ITaskService m_taskService;

  public SynchronizationContext(final IRepository repository, final long serverConnectionId,
      final IRevisionService revisionService, final IProjectService projectService, final ITaskService taskService)
      throws RepositoryException
  {
    m_repository = repository;
    m_serverConnectionId = serverConnectionId;
    m_projectService = projectService;
    m_taskService = taskService;

    m_synchronizationStatus = m_repository.getServerConnectionRepository().getSynchronizationStatus(
        m_serverConnectionId);
    m_clientRevisionStatus = m_repository.getRevisionStatus();
    m_serverRevisionStatus = revisionService.getRevisionStatus().getLatestRevisions();
  }

  public long getServerConnectionId()
  {
    return m_serverConnectionId;
  }

  public SynchronizationStatus getSynchronizationStatus(final SynchronizableType type)
  {
    return m_synchronizationStatus.get(type);
  }

  public long getClientRevisionStatus(final SynchronizableType type)
  {
    return m_clientRevisionStatus.get(type);
  }

  public long getServerRevisionStatus(final SynchronizableType type)
  {
    return m_serverRevisionStatus.get(type);
  }

  public IRepository getRepository()
  {
    return m_repository;
  }

  public IProjectService getProjectService()
  {
    return m_projectService;
  }

  public ITaskService getTaskService()
  {
    return m_taskService;
  }

}
