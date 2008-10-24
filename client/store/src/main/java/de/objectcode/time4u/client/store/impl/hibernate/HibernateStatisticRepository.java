package de.objectcode.time4u.client.store.impl.hibernate;

import java.sql.Date;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.impl.common.BaseStatisticRepository;
import de.objectcode.time4u.client.store.impl.common.IStatisticCollector;
import de.objectcode.time4u.server.entities.WorkItemEntity;

public class HibernateStatisticRepository extends BaseStatisticRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateStatisticRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  @Override
  protected void iterateWorkItems(final Date from, final Date until, final IStatisticCollector collector)
      throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<Object>() {
      public Object perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(WorkItemEntity.class);
        criteria.createAlias("dayInfo", "d");
        criteria.createAlias("project", "p");
        criteria.add(Restrictions.eq("d.person.id", m_repository.getOwner().getId()));
        criteria.add(Restrictions.ge("d.date", from));
        criteria.add(Restrictions.lt("d.date", until));
        criteria.setProjection(Projections.projectionList().add(Projections.property("d.date")).add(
            Projections.property("begin")).add(Projections.property("end")).add(Projections.property("p.id")).add(
            Projections.property("p.parentKey")).add(Projections.property("task.id")));

        final ScrollableResults results = criteria.scroll(ScrollMode.FORWARD_ONLY);

        while (results.next()) {
          final Object[] row = results.get();

          collector.collect((Date) row[0], (Integer) row[1], (Integer) row[2], (String) row[3], (String) row[4],
              (String) row[5]);
        }

        return null;
      }
    });
  }
}
