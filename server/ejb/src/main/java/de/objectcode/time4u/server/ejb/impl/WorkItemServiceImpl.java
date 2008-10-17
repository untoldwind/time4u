package de.objectcode.time4u.server.ejb.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import de.objectcode.time4u.server.api.IWorkItemService;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.DayTag;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.TimePolicy;
import de.objectcode.time4u.server.api.data.WeekTimePolicy;
import de.objectcode.time4u.server.api.data.WorkItem;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;
import de.objectcode.time4u.server.api.filter.TimePolicyFilter;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.DayTagEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.WeekTimePolicyEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.context.EntityManagerPersistenceContext;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

@Stateless
@Remote(IWorkItemService.class)
@RemoteBinding(jndiBinding = "time4u-server/WorkItemService/remote")
@SecurityDomain("time4u")
public class WorkItemServiceImpl implements IWorkItemService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private ILocalIdGenerator m_idGenerator;

  @Resource
  SessionContext m_sessionContext;

  @RolesAllowed("user")
  public DayInfo getDayInfo(final CalendarDay day)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();

    final Query query = m_manager.createQuery("from " + DayInfoEntity.class.getName()
        + " d where d.person = :person and d.date = :date");

    query.setParameter("person", person);
    query.setParameter("data", day.getDate());

    try {
      final DayInfoEntity entity = (DayInfoEntity) query.getSingleResult();

      if (entity != null) {

        final DayInfo dayInfo = new DayInfo();

        entity.toDTO(dayInfo);

        return dayInfo;
      }
    } catch (final NoResultException e) {
    }

    return null;
  }

  @RolesAllowed("user")
  public FilterResult<DayInfo> getDayInfos(final DayInfoFilter filter)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();
    final Query query = createQuery(filter, person);
    final List<DayInfo> result = new ArrayList<DayInfo>();

    for (final Object row : query.getResultList()) {
      final DayInfo dayInfo = new DayInfo();

      ((DayInfoEntity) row).toDTO(dayInfo);

      result.add(dayInfo);
    }

    return new FilterResult<DayInfo>(result);
  }

  @RolesAllowed("user")
  public FilterResult<DayInfoSummary> getDayInfoSummaries(final DayInfoFilter filter)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();
    final Query query = createQuery(filter, person);
    final List<DayInfoSummary> result = new ArrayList<DayInfoSummary>();

    for (final Object row : query.getResultList()) {
      final DayInfoSummary dayInfo = new DayInfoSummary();

      ((DayInfoEntity) row).toSummaryDTO(dayInfo);

      result.add(dayInfo);
    }

    return new FilterResult<DayInfoSummary>(result);
  }

  @RolesAllowed("user")
  public DayInfo storeDayInfo(final DayInfo dayInfo)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.DAYINFO, person.getId());

    DayInfoEntity dayInfoEntity = null;

    if (dayInfo.getId() == null) {
      dayInfo.setId(m_idGenerator.generateLocalId(EntityType.DAYINFO));
    } else {
      dayInfoEntity = m_manager.find(DayInfoEntity.class, dayInfo.getId());
    }

    if (dayInfoEntity == null) {
      dayInfoEntity = new DayInfoEntity(dayInfo.getId(), revisionLock.getLatestRevision(), dayInfo
          .getLastModifiedByClient(), person, dayInfo.getDay().getDate());

      m_manager.persist(dayInfoEntity);
    }
    if (dayInfo.getWorkItems() != null) {
      for (final WorkItem workItem : dayInfo.getWorkItems()) {
        if (workItem.getId() == null) {
          workItem.setId(m_idGenerator.generateLocalId(EntityType.WORKITEM));
        }
      }
    }
    dayInfoEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), dayInfo);
    dayInfoEntity.setRevision(revisionLock.getLatestRevision());
    dayInfoEntity.validate();

    final DayInfo result = new DayInfo();

    dayInfoEntity.toDTO(result);

    return result;
  }

  public FilterResult<TimePolicy> getTimePolicies(final TimePolicyFilter filter)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();
    final Query query = createQuery(filter, person);
    final List<TimePolicy> result = new ArrayList<TimePolicy>();

    for (final Object row : query.getResultList()) {
      if (row instanceof WeekTimePolicyEntity) {
        final WeekTimePolicy timePolicy = new WeekTimePolicy();

        ((WeekTimePolicyEntity) row).toDTO(timePolicy);

        result.add(timePolicy);
      }
    }

    return new FilterResult<TimePolicy>(result);
  }

  public TimePolicy storeTimePolicy(final TimePolicy timePolicy)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.TIMEPOLICY, person.getId());

    if (timePolicy instanceof WeekTimePolicy) {
      WeekTimePolicyEntity timePolicyEntity = null;

      if (timePolicy.getId() == null) {
        timePolicy.setId(m_idGenerator.generateLocalId(EntityType.TIMEPOLICY));
      } else {
        timePolicyEntity = m_manager.find(WeekTimePolicyEntity.class, timePolicy.getId());
      }

      if (timePolicyEntity == null) {
        timePolicyEntity = new WeekTimePolicyEntity(timePolicy.getId(), revisionLock.getLatestRevision(), timePolicy
            .getLastModifiedByClient(), person);

        m_manager.persist(timePolicyEntity);
      }

      timePolicyEntity.fromDTO((WeekTimePolicy) timePolicy);
      timePolicyEntity.setRevision(revisionLock.getLatestRevision());

      final WeekTimePolicy result = new WeekTimePolicy();

      timePolicyEntity.toDTO(result);

      return result;
    }

    return null;
  }

  public FilterResult<DayTag> getDayTags()
  {
    final Query query = m_manager.createQuery("from " + DayTagEntity.class.getName() + " t order by t.name asc");

    final List<DayTag> result = new ArrayList<DayTag>();

    for (final Object row : query.getResultList()) {
      final DayTag dayTag = new DayTag();

      ((DayTagEntity) row).toDTO(dayTag);

      result.add(dayTag);
    }

    return new FilterResult<DayTag>(result);
  }

  private Query createQuery(final DayInfoFilter filter, final PersonEntity person)
  {
    final StringBuffer queryStr = new StringBuffer("from " + DayInfoEntity.class.getName()
        + " d where d.person = :person");

    if (filter.getFrom() != null) {
      queryStr.append(" and ");
      queryStr.append("d.date >= :from");
    }
    if (filter.getTo() != null) {
      queryStr.append(" and ");
      queryStr.append("d.date < :to");
    }
    if (filter.getMinRevision() != null) {
      queryStr.append(" and ");
      queryStr.append("d.revision >= :minRevision");
    }
    if (filter.getMaxRevision() != null) {
      queryStr.append(" and ");
      queryStr.append("d.revision <= :maxRevision");
    }
    if (filter.getLastModifiedByClient() != null) {
      queryStr.append(" and ");
      queryStr.append("t.lastModifiedByClient = :lastModifiedByClient");
    }
    queryStr.append(" order by d.date");

    final Query query = m_manager.createQuery(queryStr.toString());

    query.setParameter("person", person);
    if (filter.getFrom() != null) {
      query.setParameter("from", filter.getFrom().getDate());
    }
    if (filter.getTo() != null) {
      query.setParameter("to", filter.getTo().getDate());
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

  private Query createQuery(final TimePolicyFilter filter, final PersonEntity person)
  {
    final StringBuffer queryStr = new StringBuffer("from " + TimePolicyEntity.class.getName()
        + " t where t.person = :person");

    if (filter.getDeleted() != null) {
      queryStr.append(" and ");
      queryStr.append("t.deleted = :deleted");
    }
    if (filter.getMinRevision() != null) {
      queryStr.append(" and ");
      queryStr.append("t.revision >= :minRevision");
    }
    if (filter.getMaxRevision() != null) {
      queryStr.append(" and ");
      queryStr.append("t.revision <= :maxRevision");
    }
    if (filter.getLastModifiedByClient() != null) {
      queryStr.append(" and ");
      queryStr.append("t.lastModifiedByClient = :lastModifiedByClient");
    }
    queryStr.append(" order by t.id");

    final Query query = m_manager.createQuery(queryStr.toString());
    query.setParameter("person", person);
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
