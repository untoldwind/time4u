package de.objectcode.time4u.client.connection.impl.ws;

import java.util.Map;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.server.api.IProjectService;
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
  long m_serverConnectionId;
  Map<SynchronizableType, SynchronizationStatus> m_synchronizationStatus;
  Map<SynchronizableType, Long> m_clientRevisionStatus;
  Map<SynchronizableType, Long> m_serverRevisionStatus;
  IRepository m_repository;
  IProjectService m_projectService;
  ITaskService m_taskService;

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
