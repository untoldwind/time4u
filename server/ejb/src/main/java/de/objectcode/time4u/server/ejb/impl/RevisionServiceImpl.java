package de.objectcode.time4u.server.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.RevisionStatus;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.revision.RevisionEntity;
import de.objectcode.time4u.server.entities.sync.ClientSynchronizationStatusEntity;

@Stateless
@Remote(IRevisionService.class)
@org.jboss.annotation.ejb.RemoteBinding(jndiBinding = "time4u-server/RevisionService/remote")
@org.jboss.ejb3.annotation.RemoteBinding(jndiBinding = "time4u-server/RevisionService/remote")
@org.jboss.annotation.security.SecurityDomain("time4u")
public class RevisionServiceImpl implements IRevisionService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @Resource
  SessionContext m_sessionContext;

  @RolesAllowed("user")
  public RevisionStatus getRevisionStatus()
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();

    final Map<EntityType, Long> latestRevisions = new HashMap<EntityType, Long>();

    final Query query = m_manager.createQuery("from " + RevisionEntity.class.getName()
        + " r where r.id.part in (:part1, :part2)");

    query.setParameter("part1", "<default>");
    query.setParameter("part2", person.getId());

    for (final Object row : query.getResultList()) {
      final RevisionEntity revisionEntity = (RevisionEntity) row;

      latestRevisions.put(revisionEntity.getId().getEntityType(), revisionEntity.getLatestRevision());
    }
    for (final EntityType type : EntityType.values()) {
      if (!latestRevisions.containsKey(type)) {
        latestRevisions.put(type, 0L);
      }
    }

    return new RevisionStatus(latestRevisions);
  }

  @RolesAllowed("user")
  public FilterResult<SynchronizationStatus> getClientSynchronizationStatus(final long clientId)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();
    final ClientEntity client = m_manager.find(ClientEntity.class, clientId);

    if (client == null) {
      throw new RuntimeException("Client does not exist");
    }
    if (!client.getPerson().getId().equals(person.getId())) {
      throw new RuntimeException("Client person id does not match with login");
    }

    final List<SynchronizationStatus> result = new ArrayList<SynchronizationStatus>();
    for (final ClientSynchronizationStatusEntity statusEntity : client.getSynchronizationStatus().values()) {
      final SynchronizationStatus status = new SynchronizationStatus();
      statusEntity.toDTO(status);
      result.add(status);
    }

    return new FilterResult<SynchronizationStatus>(result);
  }

  @RolesAllowed("user")
  public void storeClientSynchronizationStatus(final long clientId, final FilterResult<SynchronizationStatus> statusList)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();
    final ClientEntity client = m_manager.find(ClientEntity.class, clientId);

    if (client == null) {
      throw new RuntimeException("Client does not exist");
    }
    if (!client.getPerson().getId().equals(person.getId())) {
      throw new RuntimeException("Client person id does not match with login");
    }

    final Map<EntityType, ClientSynchronizationStatusEntity> statusEntityMap = client.getSynchronizationStatus();

    for (final SynchronizationStatus status : statusList.getResults()) {
      ClientSynchronizationStatusEntity statusEntity = statusEntityMap.get(status.getEntityType());

      if (statusEntity == null) {
        statusEntity = new ClientSynchronizationStatusEntity(client, status.getEntityType());

        statusEntity.fromDTO(status);
        m_manager.persist(statusEntity);

        statusEntityMap.put(status.getEntityType(), statusEntity);
      } else {
        statusEntity.fromDTO(status);
      }
    }
    person.setLastSynchronize(new Date());

    m_manager.flush();
  }
}
