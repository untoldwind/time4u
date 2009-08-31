package de.objectcode.time4u.client.connection.impl.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.IPersonService;
import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.ITeamService;
import de.objectcode.time4u.server.api.ITodoService;
import de.objectcode.time4u.server.api.IWorkItemService;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.ServerConnection;
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
  private final Map<EntityType, SynchronizationStatus> m_synchronizationStatus;
  private final Map<EntityType, Long> m_clientRevisionStatus;
  private final Map<EntityType, Long> m_serverRevisionStatus;
  private final IRepository m_repository;
  private final IRevisionService m_revisionService;
  private final IProjectService m_projectService;
  private final ITaskService m_taskService;
  private final IWorkItemService m_workItemService;
  private final IPersonService m_personService;
  private final ITeamService m_teamService;
  private final ITodoService m_todoService;
  private final ProjectSummary m_rootProject;
  private final String m_mappedPersonId;

  public SynchronizationContext(final IRepository repository, final ServerConnection serverConnection,
      final IRevisionService revisionService, final IProjectService projectService, final ITaskService taskService,
      final IWorkItemService workItemService, final IPersonService personService, final ITeamService teamService,
      final ITodoService todoService) throws RepositoryException
  {
    m_repository = repository;
    m_serverConnectionId = serverConnection.getId();
    m_revisionService = revisionService;
    m_projectService = projectService;
    m_taskService = taskService;
    m_workItemService = workItemService;
    m_personService = personService;
    m_teamService = teamService;
    m_todoService = todoService;
    m_mappedPersonId = serverConnection.getMappedPersonId();

    if (serverConnection.getRootProjectId() == null) {
      m_rootProject = null;
    } else {
      m_rootProject = m_repository.getProjectRepository().getProjectSummary(serverConnection.getRootProjectId());
    }

    m_synchronizationStatus = m_repository.getServerConnectionRepository().getSynchronizationStatus(
        m_serverConnectionId);
    m_clientRevisionStatus = m_repository.getRevisionStatus();
    m_serverRevisionStatus = revisionService.getRevisionStatus().getLatestRevisions();
  }

  public long getServerConnectionId()
  {
    return m_serverConnectionId;
  }

  public List<SynchronizationStatus> getSynchronizationStatusList()
  {
    return new ArrayList<SynchronizationStatus>(m_synchronizationStatus.values());
  }

  public SynchronizationStatus getSynchronizationStatus(final EntityType type)
  {
    return m_synchronizationStatus.get(type);
  }

  public long getClientRevisionStatus(final EntityType type)
  {
    return m_clientRevisionStatus.get(type);
  }

  public long getServerRevisionStatus(final EntityType type)
  {
    return m_serverRevisionStatus.get(type);
  }

  public IRepository getRepository()
  {
    return m_repository;
  }

  public IRevisionService getRevisionService()
  {
    return m_revisionService;
  }

  public IProjectService getProjectService()
  {
    return m_projectService;
  }

  public ITaskService getTaskService()
  {
    return m_taskService;
  }

  public IWorkItemService getWorkItemService()
  {
    return m_workItemService;
  }

  public IPersonService getPersonService()
  {
    return m_personService;
  }

  public ITeamService getTeamService()
  {
    return m_teamService;
  }

  public ITodoService getTodoService()
  {
    return m_todoService;
  }

  public String getMappedPersonId()
  {
    return m_mappedPersonId;
  }

  public ProjectSummary getRootProject()
  {
    return m_rootProject;
  }
}
