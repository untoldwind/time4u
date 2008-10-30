package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.client.store.api.ITeamRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.TeamRepositoryEvent;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.filter.TeamFilter;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.context.SessionPersistenceContext;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

/**
 * Hibernate implementation of the team interface.
 * 
 * @author junglas
 */
public class HibernateTeamRepository implements ITeamRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateTeamRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  public List<Team> getTeams(final TeamFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<Team>>() {
      public List<Team> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(TeamEntity.class);

        if (filter.getDeleted() != null) {
          criteria.add(Restrictions.eq("deleted", filter.getDeleted()));
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        if (filter.getMaxRevision() != null) {
          criteria.add(Restrictions.le("revision", filter.getMaxRevision()));
        }
        if (filter.getLastModifiedByClient() != null) {
          criteria.add(Restrictions.eq("lastModifiedByClient", filter.getLastModifiedByClient()));
        }
        switch (filter.getOrder()) {
          case ID:
            criteria.addOrder(Order.asc("id"));
            break;
          case NAME:
            criteria.addOrder(Order.asc("name"));
            criteria.addOrder(Order.asc("id"));
            break;
        }

        final List<Team> result = new ArrayList<Team>();

        for (final Object row : criteria.list()) {
          final Team team = new Team();

          ((TeamEntity) row).toDTO(team);

          result.add(team);
        }

        return result;
      }
    });
  }

  public List<TeamSummary> getTeamSummaries(final TeamFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<TeamSummary>>() {
      public List<TeamSummary> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(TeamEntity.class);

        if (filter.getDeleted() != null) {
          criteria.add(Restrictions.eq("deleted", filter.getDeleted()));
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        if (filter.getMaxRevision() != null) {
          criteria.add(Restrictions.le("revision", filter.getMaxRevision()));
        }
        if (filter.getLastModifiedByClient() != null) {
          criteria.add(Restrictions.eq("lastModifiedByClient", filter.getLastModifiedByClient()));
        }
        switch (filter.getOrder()) {
          case ID:
            criteria.addOrder(Order.asc("id"));
            break;
          case NAME:
            criteria.addOrder(Order.asc("name"));
            criteria.addOrder(Order.asc("id"));
            break;
        }

        final List<TeamSummary> result = new ArrayList<TeamSummary>();

        for (final Object row : criteria.list()) {
          final TeamSummary team = new TeamSummary();

          ((TeamEntity) row).toSummaryDTO(team);

          result.add(team);
        }

        return result;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public void storeTeam(final Team team, final boolean modifiedByOwner) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation() {
      public void perform(final Session session)
      {
        final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);
        final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.TEAM, null);

        if (team.getId() == null) {
          team.setId(m_repository.generateLocalId(EntityType.TEAM));
        }
        final TeamEntity teamEntity = new TeamEntity(team.getId(), revisionLock.getLatestRevision(), m_repository
            .getClientId(), team.getName());

        teamEntity.fromDTO(new SessionPersistenceContext(session), team);
        if (modifiedByOwner) {
          teamEntity.setLastModifiedByClient(m_repository.getClientId());
        }

        session.merge(teamEntity);
        session.flush();

        teamEntity.toDTO(team);
      }
    });

    m_repository.fireRepositoryEvent(new TeamRepositoryEvent(team));
  }
}
