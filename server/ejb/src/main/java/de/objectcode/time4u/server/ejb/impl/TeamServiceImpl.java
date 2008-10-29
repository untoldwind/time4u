package de.objectcode.time4u.server.ejb.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.RemoteBinding;

import de.objectcode.time4u.server.api.ITeamService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.filter.TeamFilter;
import de.objectcode.time4u.server.entities.TeamEntity;

@Stateless
@Remote(ITeamService.class)
@RemoteBinding(jndiBinding = "time4u-server/TeamService/remote")
public class TeamServiceImpl implements ITeamService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @Resource
  SessionContext m_sessionContext;

  @RolesAllowed("user")
  public FilterResult<Team> getTeams(final TeamFilter filter)
  {
    final Query query = createQuery(filter);
    final List<Team> result = new ArrayList<Team>();

    for (final Object row : query.getResultList()) {
      final Team team = new Team();

      ((TeamEntity) row).toDTO(team);

      result.add(team);
    }

    return new FilterResult<Team>(result);
  }

  private Query createQuery(final TeamFilter filter)
  {
    String combineStr = " where ";
    final StringBuffer queryStr = new StringBuffer("from " + TeamEntity.class.getName() + " t");

    if (filter.getDeleted() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.deleted = :deleted");
      combineStr = " and ";
    }

    if (filter.getMinRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.revision >= :minRevision");
      combineStr = " and ";
    }
    if (filter.getMaxRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.revision <= :maxRevision");
      combineStr = " and ";
    }
    if (filter.getLastModifiedByClient() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.lastModifiedByClient = :lastModifiedByClient");
      combineStr = " and ";
    }

    switch (filter.getOrder()) {
      case ID:
        queryStr.append(" order by t.id asc");
        break;
      case NAME:
        queryStr.append(" order by t.name asc, t.id asc");
        break;
    }

    final Query query = m_manager.createQuery(queryStr.toString());

    if (filter.getDeleted() != null) {
      query.setParameter("deleted", filter.getDeleted());
    }
    if (filter.getMinRevision() != null) {
      query.setParameter("minRevision", filter.getMinRevision());
    }
    if (filter.getMaxRevision() != null) {
      query.setParameter("maxRevision", filter.getMaxRevision());
    }
    if (filter.getLastModifiedByClient() != null) {
      query.setParameter("lastModifiedByClient", filter.getLastModifiedByClient());
    }

    return query;
  }

}
