package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.client.store.api.IWorkItemRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.WorkItem;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.context.SessionPersistenceContext;

public class HibernateWorkItemRepository implements IWorkItemRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateWorkItemRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public DayInfo getDayInfo(final CalendarDay day) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<DayInfo>() {
      public DayInfo perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(DayInfoEntity.class);
        criteria.add(Restrictions.eq("person.id", m_repository.getOwner().getId()));
        criteria.add(Restrictions.eq("date", day.getDate()));

        final DayInfoEntity entity = (DayInfoEntity) criteria.uniqueResult();

        if (entity == null) {
          return null;
        }

        final DayInfo dayInfo = new DayInfo();

        entity.toDTO(dayInfo);

        return dayInfo;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public List<DayInfoSummary> getDayInfoSummaries(final DayInfoFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<List<DayInfoSummary>>() {
      public List<DayInfoSummary> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(DayInfoEntity.class);
        criteria.add(Restrictions.eq("person.id", m_repository.getOwner().getId()));
        if (filter.getFrom() != null) {
          criteria.add(Restrictions.ge("date", filter.getFrom().getDate()));
        }
        if (filter.getTo() != null) {
          criteria.add(Restrictions.lt("date", filter.getTo().getDate()));
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        criteria.addOrder(Order.asc("date"));

        final List<?> result = criteria.list();
        final List<DayInfoSummary> dayInfos = new ArrayList<DayInfoSummary>();

        for (final Object row : result) {
          final DayInfoEntity entity = (DayInfoEntity) row;
          final DayInfoSummary dayInfo = new DayInfoSummary();

          entity.toSummaryDTO(dayInfo);
          dayInfos.add(dayInfo);
        }

        return dayInfos;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public WorkItem storeWorkItem(final WorkItem workItem) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<WorkItem>() {
      public WorkItem perform(final Session session)
      {
        WorkItemEntity workItemEntity;

        if (workItem.getId() > 0L) {
          workItemEntity = (WorkItemEntity) session.get(WorkItemEntity.class, workItem.getId());

          session.lock(workItemEntity.getDayInfo(), LockMode.UPGRADE);

          workItemEntity.fromDTO(new SessionPersistenceContext(session), workItem);

          session.flush();
        } else {
          final Criteria criteria = session.createCriteria(DayInfoEntity.class);
          criteria.add(Restrictions.eq("person.id", m_repository.getOwner().getId()));
          criteria.add(Restrictions.eq("date", workItem.getDay().getDate()));
          criteria.setLockMode(LockMode.UPGRADE);

          DayInfoEntity dayInfoEntity = (DayInfoEntity) criteria.uniqueResult();

          if (dayInfoEntity == null) {
            try {
              createDayInfo(workItem.getDay());
            } catch (final Exception e) {
            }
            dayInfoEntity = (DayInfoEntity) criteria.uniqueResult();

            if (dayInfoEntity == null) {
              throw new RuntimeException("Failed to create and lock dayinfo: " + workItem.getDay());
            }
          }
          workItemEntity = new WorkItemEntity();

          workItemEntity.setDayInfo(dayInfoEntity);
          workItemEntity.fromDTO(new SessionPersistenceContext(session), workItem);

          session.persist(workItemEntity);
          session.flush();
        }

        final WorkItem workItem = new WorkItem();

        workItemEntity.toDTO(workItem);

        return workItem;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public WorkItem getActiveWorkItem()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Create a dayinfo in a separate transaction.
   * 
   * Note that this might fail in a multi-thread environment.
   * 
   * @param day
   *          The calendar day of the dayinfo
   * @return The summary of the new dayinfo
   * @throws RepositoryException
   *           on error
   */
  private DayInfoSummary createDayInfo(final CalendarDay day) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<DayInfoSummary>() {
      public DayInfoSummary perform(final Session session)
      {
        final DayInfoEntity entity = new DayInfoEntity();
        entity.setDate(day.getDate());
        entity.setPerson((PersonEntity) session.get(PersonEntity.class, m_repository.getOwner().getId()));

        session.persist(entity);
        session.flush();

        final DayInfoSummary dayinfo = new DayInfoSummary();

        entity.toSummaryDTO(dayinfo);

        return dayinfo;
      }
    });
  }

}
