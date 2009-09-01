package de.objectcode.time4u.server.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.objectcode.time4u.server.api.IPersonService;
import de.objectcode.time4u.server.api.data.ClientIdList;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Stateless
@Remote(IPersonService.class)
@org.jboss.annotation.ejb.RemoteBinding(jndiBinding = "time4u-server/PersonService/remote")
@org.jboss.ejb3.annotation.RemoteBinding(jndiBinding = "time4u-server/PersonService/remote")
public class PersonServiceImpl implements IPersonService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @Resource
  SessionContext m_sessionContext;

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public Person getSelf()
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());

    final Person person = new Person();

    userAccount.getPerson().toDTO(person);

    return person;
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public boolean registerClient(final long clientId)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());

    ClientEntity clientEntity = m_manager.find(ClientEntity.class, clientId);

    if (clientEntity != null) {
      return userAccount.getPerson().getId().equals(clientEntity.getPerson().getId());
    }

    clientEntity = new ClientEntity();
    clientEntity.setClientId(clientId);
    clientEntity.setPerson(userAccount.getPerson());
    clientEntity.setRegisteredAt(new Date());

    m_manager.persist(clientEntity);

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @RolesAllowed("user")
  public ClientIdList getRegisteredClients()
  {
    final Query query = m_manager.createQuery("select c.clientId from " + ClientEntity.class.getName() + " c");
    final List<Long> result = query.getResultList();

    return new ClientIdList(result);
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public FilterResult<Person> getPersons(final PersonFilter filter)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final Set<String> allowedTeamIds = new HashSet<String>();

    for (final TeamEntity team : userAccount.getPerson().getResponsibleFor()) {
      allowedTeamIds.add(team.getId());
    }
    for (final TeamEntity team : userAccount.getPerson().getMemberOf()) {
      allowedTeamIds.add(team.getId());
    }

    final Query query = createQuery(filter);
    final List<Person> result = new ArrayList<Person>();

    for (final Object row : query.getResultList()) {
      final PersonEntity personEntity = (PersonEntity) row;

      for (final TeamEntity team : personEntity.getMemberOf()) {
        if (allowedTeamIds.contains(team.getId())) {
          final Person person = new Person();

          personEntity.toDTO(person);

          result.add(person);
          break;
        }
      }
    }

    return new FilterResult<Person>(result);
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public FilterResult<PersonSummary> getPersonSummaries(final PersonFilter filter)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final Set<String> allowedTeamIds = new HashSet<String>();

    for (final TeamEntity team : userAccount.getPerson().getResponsibleFor()) {
      allowedTeamIds.add(team.getId());
    }
    for (final TeamEntity team : userAccount.getPerson().getMemberOf()) {
      allowedTeamIds.add(team.getId());
    }

    final Query query = createQuery(filter);
    final List<PersonSummary> result = new ArrayList<PersonSummary>();

    for (final Object row : query.getResultList()) {
      final PersonEntity personEntity = (PersonEntity) row;

      for (final TeamEntity team : personEntity.getMemberOf()) {
        if (allowedTeamIds.contains(team.getId())) {
          final PersonSummary person = new Person();

          personEntity.toSummaryDTO(person);

          result.add(person);
          break;
        }
      }
    }

    return new FilterResult<PersonSummary>(result);
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public Person getPerson(final String personId)
  {
    final PersonEntity personEntity = m_manager.find(PersonEntity.class, personId);

    if (personEntity != null) {
      final Person person = new Person();

      personEntity.toDTO(person);

      return person;
    }

    return null;
  }

  private Query createQuery(final PersonFilter filter)
  {
    String combineStr = " where ";
    final StringBuffer queryStr = new StringBuffer("from " + PersonEntity.class.getName() + " p");

    if (filter.getDeleted() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.deleted = :deleted");
      combineStr = " and ";
    }
    if (filter.getMinRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.revision >= :minRevision");
      combineStr = " and ";
    }
    if (filter.getMaxRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.revision <= :maxRevision");
      combineStr = " and ";
    }
    if (filter.getLastModifiedByClient() != null) {
      queryStr.append(combineStr);
      queryStr.append("p.lastModifiedByClient = :lastModifiedByClient");
      combineStr = " and ";
    }
    if (filter.getMemberOfTeamId() != null) {
      queryStr.append(combineStr);
      queryStr.append("some(p.memberOf.id) = :memberOfTeamId");
      combineStr = " and ";
    }

    switch (filter.getOrder()) {
      case ID:
        queryStr.append(" order by p.id asc");
        break;
      case NAME:
        queryStr.append(" order by p.surname asc, p.givenName asc, p.id asc");
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
    if (filter.getMemberOfTeamId() != null) {
      query.setParameter("memberOfTeamId", filter.getMemberOfTeamId());
    }

    return query;
  }
}
