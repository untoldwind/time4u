package de.objectcode.time4u.server.ejb.impl;

import java.util.HashMap;
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
import de.objectcode.time4u.server.api.data.RevisionStatus;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.revision.RevisionEntity;

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
}
