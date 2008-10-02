package de.objectcode.time4u.server.ejb.impl;

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
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;
import de.objectcode.time4u.server.ejb.config.IConfigServiceLocal;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.context.EntityManagerPersistenceContext;
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
  private IConfigServiceLocal m_configService;

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
    // TODO Auto-generated method stub
    return null;
  }

  @RolesAllowed("user")
  public FilterResult<DayInfoSummary> getDayInfoSummaries(final DayInfoFilter filter)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @RolesAllowed("user")
  public DayInfo storeDayInfo(final DayInfo dayInfo)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final PersonEntity person = userAccount.getPerson();
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(SynchronizableType.DAYINFO, person.getId());

    DayInfoEntity dayInfoEntity = null;

    if (dayInfo.getId() == null) {
      dayInfo.setId(revisionLock.generateId(m_configService.getServerId()));
    } else {
      dayInfoEntity = m_manager.find(DayInfoEntity.class, dayInfo.getId());
    }

    if (dayInfoEntity == null) {
      dayInfoEntity = new DayInfoEntity(dayInfo.getId(), revisionLock.getLatestRevision(), dayInfo
          .getLastModifiedByClient(), person, dayInfo.getDay().getDate());

      m_manager.persist(dayInfoEntity);
    }
    dayInfoEntity.fromDTO(new EntityManagerPersistenceContext(m_manager), dayInfo);
    dayInfoEntity.setRevision(revisionLock.getLatestRevision());
    dayInfoEntity.validate();

    final DayInfo result = new DayInfo();

    dayInfoEntity.toDTO(result);

    return result;
  }

}
