package de.objectcode.time4u.client.store.impl.hibernate;

import java.sql.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.impl.common.BaseStatisticRepository;
import de.objectcode.time4u.client.store.impl.common.IStatisticCollector;
import de.objectcode.time4u.server.entities.WorkItemEntity;

public class HibernateStatisticRepository extends BaseStatisticRepository
{
  private final HibernateTemplate m_hibernateTemplate;

  HibernateStatisticRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_hibernateTemplate = hibernateTemplate;
  }

  @Override
  protected void iterateWorkItems(final Date from, final Date until, final IStatisticCollector collector)
      throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<Object>() {
      public Object perform(final Session session)
      {
        final Query query = session
            .createQuery("select w.dataInfo.date, w.begin, w.end, w.project.id, w.project.parentKey, w.task.id from "
                + WorkItemEntity.class.getName() + " w where w.dayInfo.date >= :from and w.dayInfo.date < :until");

        query.setParameter("from", from);
        query.setParameter("until", until);

        final Iterator<?> it = query.iterate();

        while (it.hasNext()) {
          final Object[] row = (Object[]) it.next();

          collector.collect((Date) row[0], (Integer) row[1], (Integer) row[2], (String) row[3], (String) row[4],
              (String) row[5]);
        }

        return null;
      }
    });
  }
}
